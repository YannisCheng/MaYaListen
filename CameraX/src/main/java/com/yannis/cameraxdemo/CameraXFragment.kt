package com.yannis.cameraxdemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.File

/**
 * CameraXFragment.kt  CameraX使用
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/10/21 - 13:59
 */
const val TAG = "CameraX"

class CameraXFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate outputFile: ${getOutputFile()}")
    }

    /**
     * 设置保存照片的文件
     */
    private fun getOutputFile(): File {
        val outputFile = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, requireContext().packageName).apply { mkdirs() }
        }
        return if (outputFile != null && outputFile.exists()) outputFile else requireContext().filesDir
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_x, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(/*param1: String*/) =
            CameraXFragment().apply {
//                arguments = Bundle().apply {
//                    putString(OUTPUT_FILE, param1)
//                }
            }
    }
}