package com.yannis.baselib.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yannis.baselib.utils.status_bar.BarStatusAndStyleUtils
import com.yannis.baselib.widget.LoadingDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseActivity 基类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseActivity<VM : ViewModel, VDB : ViewDataBinding> : AppCompatActivity() {
    private val REQUEST_EXTERNAL_STORAGE: Int = 1

    lateinit var binding: VDB
    lateinit var viewModel: VM

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
        // 初始化 DataViewBInding、ViewBinding、ViewModel
        initBinding()
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showFilePermission()
        }
        loadData()
        refreshData()
        dataToView()
    }

    abstract fun dataToView()

    open fun refreshData() {

    }

    open fun loadData() {

    }

    private fun initBinding() {
        val inflater = DataBindingUtil.inflate<VDB>(layoutInflater, getLayoutId(), null, false)
        if (ViewModel::class.java != setBindViewModel()) {
            viewModel = ViewModelProvider(this)[setBindViewModel()]
            // -- databinding --
            //inflater.setVariable(BR.itemMode, viewModel)
            setVariables(inflater)
            inflater.lifecycleOwner = this
            // -- databinding --
        }
        binding = inflater as VDB
        setContentView(binding.root)
        BarStatusAndStyleUtils.setStatusBarDarkTheme(this@BaseActivity, true)
    }

    /**
     * 设置view中的绑定值
     */
    fun setVariables(inflater: VDB) {}

    abstract fun setBindViewModel(): Class<VM>

    /**
     * 初始化view布局
     */
    abstract fun initView()

    /**
     * 获取布局文件id
     */
    abstract fun getLayoutId(): Int

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