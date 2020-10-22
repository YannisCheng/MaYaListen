package com.yannis.cameraxdemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera_x.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


private const val TAG = "CameraX"
private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

/**
 * CameraXFragment.kt  CameraX使用
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/10/21 - 13:59
 */
class CameraXFragment : Fragment() {

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

        preview = Preview.Builder().build()

        imageCapture = ImageCapture.Builder().build()

        imageAnalysis = ImageAnalysis.Builder().build()

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
    }

}