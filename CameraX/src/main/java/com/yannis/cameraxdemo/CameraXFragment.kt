package com.yannis.cameraxdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_camera_x.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


private const val DEBUG_TAG = "CameraX"
private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

/**
 * CameraXFragment.kt  CameraX使用
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/10/21 - 13:59
 */
class CameraXFragment : Fragment(), GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private var isOpenFlashMode: Boolean = false

    //- - - - -
    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView

    private lateinit var camera: Camera
    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalysis: ImageAnalysis

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var disPlayId: Int = -1;
    private lateinit var outputFile: File

    private lateinit var cameraExecutor: ExecutorService

    private val disPlayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit

        override fun onDisplayRemoved(displayId: Int) = Unit

        override fun onDisplayChanged(displayId: Int) {
            Log.e(TAG, "onDisplayChanged: $displayId")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(/*param1: String*/) =
            CameraXFragment().apply {
//                arguments = Bundle().apply {
//                    putString(OUTPUT_FILE, param1)
//                }
            }

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private fun getOutputFile(): File {
        val outputFile = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, "camreaX").apply { mkdirs() }
        }
        return if (outputFile != null && outputFile.exists()) outputFile else requireContext().filesDir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate outputFile: ${getOutputFile()}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera_x, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.viewFinder)

        cameraExecutor = Executors.newSingleThreadExecutor()

        disPlayManager.registerDisplayListener(displayListener, null)
        outputFile = getOutputFile()
        permissionGranted()
    }

    override fun onResume() {
        super.onResume()
        permissionGranted()
    }

    private fun permissionGranted() {
        if (!hasPermissions(requireContext())) {
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // 跳转
            prepareInit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCameraUI()
        updateSwitchCameraButton()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // 授予权限后，将用户带到成功片段
            } else {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun prepareInit() {
        viewFinder.post {
            disPlayId = viewFinder.display.displayId
            updateCameraUI()
            setUpCamera()
        }
    }


    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        // 在主线程（main-UI）中处理
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            updateSwitchCameraButton()
            bindCameraUseCase()

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCase() {

        // 获取用于设置摄像头以实现全屏分辨率的屏幕指标
        val metrics = DisplayMetrics().also {
            viewFinder.display.getRealMetrics(it)
        }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = viewFinder.display.rotation
        Log.e(TAG, "Screen matrics ${metrics.widthPixels} x ${metrics.heightPixels}")
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")
        Log.e(TAG, "rotation: $rotation")

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder()
            // 拍摄模式最小化延迟
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            //我们要求宽高比，但没有分辨率以匹配预览配置，但是CameraX可以针对最适合我们用例的特定分辨率进行优化
            .setTargetAspectRatio(screenAspectRatio)
            // 设置初始目标旋转，如果在此用例的生命周期内旋转发生变化，我们将不得不再次调用它
            .setTargetRotation(rotation)
            .build()

        imageAnalysis = ImageAnalysis.Builder()
            .build()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(
            this,
            cameraSelector,
            preview,
            imageCapture,
            imageAnalysis
        )
        preview.setSurfaceProvider(viewFinder.surfaceProvider)
        pointFocusByHand(metrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun pointFocusByHand(metrics: DisplayMetrics) {
        /*viewFinder.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {

                    val factory = SurfaceOrientedMeteringPointFactory(
                        metrics.widthPixels.toFloat(),
                        metrics.heightPixels.toFloat()
                    )
                    val point = factory.createPoint(event.x, event.y)
                    val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                        .addPoint(point, FocusMeteringAction.FLAG_AWB)
                        .setAutoCancelDuration(3, TimeUnit.SECONDS)
                        .build()

                    val future = camera.cameraControl.startFocusAndMetering(action)
                    future.addListener({
                        val result = future.get()
                        if (result.isFocusSuccessful) {
                            Log.e(TAG, "onTouch: true")
                        } else {
                            Log.e(TAG, "onTouch: false")
                        }

                    }, ContextCompat.getMainExecutor(requireContext()))
                }
            }
            true
        }*/
    }

    private fun updateSwitchCameraButton() {
        val switchCamerasButton = container.findViewById<Button>(R.id.camera_switch)
        try {
            switchCamerasButton.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            switchCamerasButton.isEnabled = false
        }
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    //通过将预览比率的绝对值计数为提供的值之一，来检测@params中提供的尺寸的最合适比率。
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        return if (abs(previewRatio - AspectRatio.RATIO_4_3) <= abs(previewRatio - AspectRatio.RATIO_16_9))
            AspectRatio.RATIO_4_3
        else
            AspectRatio.RATIO_16_9
    }

    private fun updateCameraUI() {

        camera_switch.let {
            it.setOnClickListener {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT)
                    CameraSelector.LENS_FACING_BACK
                else
                    CameraSelector.LENS_FACING_FRONT

                bindCameraUseCase()
            }
        }

        camera_flash.setOnClickListener {
            if (isOpenFlashMode) {
                isOpenFlashMode = false
                camera.cameraControl.enableTorch(false)
            } else {
                isOpenFlashMode = true
                camera.cameraControl.enableTorch(true)
            }
        }

        take_photo.setOnClickListener {

            val photoFile = File(
                getOutputFile(),
                SimpleDateFormat(
                    FILENAME,
                    Locale.CHINA
                ).format(System.currentTimeMillis()) + PHOTO_EXTENSION
            )

            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(metadata)
                .build()

            imageCapture.flashMode = ImageCapture.FLASH_MODE_AUTO
            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val resultUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                        ToastUtils.showShort("保存成功，路径为：$resultUri")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        ToastUtils.showShort("保存失败：${exception.toString()}")
                    }

                })
        }
    }


    override fun onDown(event: MotionEvent?): Boolean {
        Log.e(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.e(DEBUG_TAG, "onShowPress: $e")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.e(DEBUG_TAG, "onSingleTapUp: $e")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.e(DEBUG_TAG, "onScroll: $e1 $e2")
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.e(DEBUG_TAG, "onLongPress: $e")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.e(DEBUG_TAG, "onFling: $e1 $e2")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        Log.e(DEBUG_TAG, "onSingleTapConfirmed: $event")
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.e(DEBUG_TAG, "onDoubleTap: $e")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.e(DEBUG_TAG, "onDoubleTapEvent: $e")
        return true
    }


}