package com.google.zxing.client.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    val REQUEST_SCAN_QRCODE = 0X11
    private val PERMISSION_REQUEST: Int = 1001
    private val PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private val NO_PASS_PERMISSIONS: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission()
        }
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //清空已经允许的没有通过的权限
            NO_PASS_PERMISSIONS.clear()

            //逐个判断是否还有未通过的权限
            for (item in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        item
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    NO_PASS_PERMISSIONS.add(item)
                }
            }
            if (NO_PASS_PERMISSIONS.size > 0) {
                //有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST)
            } else {
                // 权限已经通过
                permissionOk()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //有权限没有通过
        var hasPermissionDismiss = false

        if (requestCode == PERMISSION_REQUEST) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true
                    break
                }
            }
        }

        if (hasPermissionDismiss) {
            //如果有没有被允许的权限
            //showPermissionDialog()
        } else {
            //权限已经都通过了，可以将程序继续打开了
            permissionOk()
        }
    }

    private fun permissionOk() {
        scanQRCode()
    }

    private fun scanQRCode() {
        val scanIntent = Intent(this, CaptureActivity::class.java)
        scanIntent.action = Intents.Scan.ACTION
        // scanIntent.putExtra(Intents.Scan.WIDTH, 99999);
        // scanIntent.putExtra(Intents.Scan.HEIGHT, 99999);
        startActivityForResult(scanIntent, REQUEST_SCAN_QRCODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            REQUEST_SCAN_QRCODE -> {
                if (resultCode == RESULT_OK) {
                    val bundle: Bundle? = data?.extras
                    val resultStr: String? = bundle?.getString(Intents.Scan.RESULT);
                    resultStr?.let {
                        Toast.makeText(this, resultStr, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}