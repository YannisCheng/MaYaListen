package com.yannis.cameraxdemo

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraX"
        private const val FILE_NAME_FROMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSION = 1;
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var pixelX: Float = 0f
    private var pixelY: Float = 0f
    private var rotation: Int = 0

    // 默认选择后置摄像头
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 0.检查权限
        if (allPermissionGranted()) {
            startCameraX()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
        }

        // 2.拍照
        camera_capture_button.setOnClickListener { takePhoto() }
        // 1-1.设置文件保存路径
        outputDirectory = getOutputDirectory()
        // 1.线程
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // 1.文件保存位置
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }

        }
        Log.e(TAG, "getOutputDirectory: " + mediaDir?.absolutePath)
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    // 3.图片采集
    private fun takePhoto() {
        // 获得有关可修改图像捕获用例的稳定参考
        val imageCapture = imageCapture ?: return

        // 创建带时间戳的输出文件以保存图像
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILE_NAME_FROMAT,
                Locale.CHINA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // 创建包含文件+元数据的输出选项对象
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }

            })
    }

    // 2.相机准备
    private fun startCameraX() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // 用于将摄像机的生命周期绑定到生命周期所有者
            val cameraProvider = cameraProviderFuture.get()
            // 预览
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }
            // 初始化ImageCapture用例，供takePhoto()使用
            imageCapture = ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            setOrientationListener()

            // 在ImageAnalysis中实例化LuminosityAnalyzer的实例
            val imageAnalyzer = ImageAnalysis.Builder()
                // 分辨率，最大支持1080p
                .setTargetResolution(Size(1280, 720))
                // 仅将最新图像传送到分析仪，并在到达图像时将其丢弃。
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        //Log.e(TAG, "startCameraX: $luma")
                    })
                }
//Face
            // 重新绑定之前取消绑定用例
            cameraProvider.unbindAll()
            // 将用例绑定到相机
            val camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalyzer
            )

            setPointAutoFocus(camera)

            camera_choose.setOnClickListener {
                when (cameraSelector) {
                    CameraSelector.DEFAULT_BACK_CAMERA -> {
                        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                        camera_choose.text = "Back"
                    }
                    CameraSelector.DEFAULT_FRONT_CAMERA -> {
                        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        camera_choose.text = "front"
                    }
                }
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun setOrientationListener() {
        var orientationEventListener: OrientationEventListener = object :
            OrientationEventListener(baseContext) {
            override fun onOrientationChanged(orientation: Int) {
                rotation = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                Log.e(TAG, "onOrientationChanged: $rotation")
                imageCapture?.targetRotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    // 设置点按自动对焦
    private fun setPointAutoFocus(camera: Camera) {
        val cameraControl = camera.cameraControl
        val factory = SurfaceOrientedMeteringPointFactory(1080f, 1920f)
        val point = factory.createPoint(pixelX, pixelY)
        // val point2 = factory.createPoint(500f, 900f)
        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
            //.addPoint(point2, FocusMeteringAction.FLAG_AF)
            .setAutoCancelDuration(5, TimeUnit.SECONDS)
            .build()
        val future = cameraControl.startFocusAndMetering(action)
        future.addListener(Runnable {
            val get = future.get()
            Log.e(TAG, "startCameraX isFocusSuccessful is : " + get.isFocusSuccessful)
        }, cameraExecutor)
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED

    }

    // 0 权限处理
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                startCameraX()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            //将缓冲区倒回零
            rewind()
            val data = ByteArray(remaining())
            //将缓冲区复制到字节数组
            get(data)
            return data
        }

        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()
            listener(luma)
            image.close()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {

            // 点击的开始位置
            MotionEvent.ACTION_DOWN -> {
                pixelX = event.x
                pixelY = event.y
            }

            // 触屏实时(滑动)位置
            MotionEvent.ACTION_MOVE -> {
                pixelX = event.x
                pixelY = event.y
            }

            // 离开屏幕的位置
            MotionEvent.ACTION_UP -> {
                pixelX = event.x
                pixelY = event.y
            }
        }

        return true
    }


}
