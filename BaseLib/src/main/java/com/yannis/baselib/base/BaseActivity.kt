package com.yannis.baselib.base

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.yannis.baselib.utils.net_status.NetStatus
import com.yannis.baselib.utils.net_status.NetStatusChange
import com.yannis.baselib.utils.net_status.NetStatusManager
import com.yannis.baselib.utils.net_status.NetStatusUtils
import com.yannis.baselib.utils.status_bar.StatusBarUtils
import com.yannis.baselib.widget.LoadingDialog


/**
 * BaseActivity Activity基类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseActivity<VM : ViewModel, VDB : ViewDataBinding> : AppCompatActivity() {
    private val PERMISSION_REQUEST: Int = 1001

    lateinit var binding: VDB
    lateinit var viewModel: VM

    private val PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CALL_PHONE,
        // 如果您的App需要上传到google play store，您需要将READ_PHONE_STATE权限屏蔽掉或者移除，否则可能会被下架
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.GET_ACCOUNTS
    )

    private val NO_PASS_PERMISSIONS: ArrayList<String> = ArrayList<String>()

    var mPermissionDialog: AlertDialog? = null

    private lateinit var loadingDialog: LoadingDialog

    fun showLoading(msg: String) {
        loadingDialog = LoadingDialog
            .Builder(this)
            .setMsg(msg)
            .isCancelOutSide(false)
            .isCancelable(false)
            .create()
        loadingDialog.show()
    }

    fun hidingLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 当在调用 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)时，
        // 此时需要在此回调方法中进行设置，否则当前activity会重建。
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        /*when(currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Log.e("TAG", "onConfigurationChanged: MODE_NIGHT_NO")
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Log.e("TAG", "onConfigurationChanged: MODE_NIGHT_YES")
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 DataViewBInding、ViewBinding、ViewModel
        initBinding()
        NetStatusManager.getInstance(this.application).register(this)
        val netType = NetStatusManager.getInstance(this.application).getNetType()
        initAppNetStatus(netType)
        initView();
        // 权限处理
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission()
        }*/
        loadData()
        refreshData()
        dataToView()
    }

    private fun initAppNetStatus(netType: String) {
        Log.e("NetManager", "type is : $netType")
        if (netType != NetStatus.NONE) {
            Log.e("NetManager", "初始：网络已经连接")
        }
        if (netType === NetStatus.WIFI) {
            if (NetStatusUtils.is5GWifiConnected(this)) {
                Log.e("NetManager", "这是5G WI-FI")
            } else {
                Log.e("NetManager", "这是2.4G WI-FI")
            }
            Log.e("NetManager", "WI-FI名：${NetStatusUtils.getConnectedWifiSSID(this)}")
        }
    }

    @NetStatusChange
    fun onNetStatusChange(status: @NetStatus String) {
        Log.e("NetManager", "Main网络状态改变：${status}")
        if (status === NetStatus.OK) {
            ToastUtils.showShort("网络已经连接")
        } else if (status === NetStatus.NONE) {
            ToastUtils.showShort("网络已经断开")
        }

        if (status === NetStatus.WIFI) {
            if (NetStatusUtils.is5GWifiConnected(this)) {
                Log.e("NetManager", "这是5G WI-FI")
            } else {
                Log.e("NetManager", "这是2.4G WI-FI")
            }
            Log.e("NetManager", "WI-FI名：${NetStatusUtils.getConnectedWifiSSID(this)}")
        } else if (status == NetStatus.CELLULAR) {
            Log.e("NetManager", "这是 蜂窝网络")
        } else if (status == NetStatus.NET_UNKNOWN) {
            Log.e("NetManager", "这是 未知网络")
        }
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
        StatusBarUtils.setStatusBarDarkTheme(this@BaseActivity, true)
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

    public fun requestPermission() {
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

    abstract fun permissionOk()

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
            showPermissionDialog()
        } else {
            //权限已经都通过了，可以将程序继续打开了
            permissionOk()
        }
    }

    private fun showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = AlertDialog.Builder(this)
                .setMessage("已禁用权限，请手动授予")
                .setPositiveButton("设置",
                    DialogInterface.OnClickListener { dialog, which ->
                        cancelPermissionDialog()
                        val packageURI: Uri = Uri.parse("package:${AppUtils.getAppPackageName()}")
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        startActivity(intent)
                    })
                .setNegativeButton("取消",
                    DialogInterface.OnClickListener { dialog, which ->
                        //关闭页面或者做其他操作
                        cancelPermissionDialog()
                        this@BaseActivity.finish()
                    })
                .create()
        }
        mPermissionDialog!!.show()
    }

    private fun cancelPermissionDialog() {
        mPermissionDialog!!.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        NetStatusManager.getInstance(this.application).unRegister(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //UmengInit.QQCallBack(this, requestCode, resultCode, data!!)
    }
}