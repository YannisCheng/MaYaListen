package com.yannis.mayalisten.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.yannis.mayalisten.widget.LoadingDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseActivity : AppCompatActivity() {
    private val REQUEST_EXTERNAL_STORAGE: Int = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.Manifest.permission.CAMERA"
    )
    var compositeDisposable: CompositeDisposable? = null
    val builder: LoadingDialog.Builder = LoadingDialog.Builder(this)

    fun showLoading(msg: String) {
        val create = LoadingDialog
            .Builder(this)
            .setMsg(msg)
            .isCancelOutSide(false)
            .isCancelable(false)
            .create()
        create.show()
    }

    fun hidingLoding() {

    }

    fun addDispose(disposable: Disposable) {
        compositeDisposable?.add(disposable) ?: CompositeDisposable()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFilePermission()
        }
    }

    private fun showFilePermission() {
        val checkSelfPermission =
            ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        } else {
            Toast.makeText(this, "已获取文件权限", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已获取文件权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "未获取文件权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.let {
            compositeDisposable?.clear()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*if (popupChoose != null && popupChoose.isShowing()) {
                popupChoose.dismiss()
                return true
            } else {

            }*/
            backClickListener()
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun backClickListener() {

    }
}