package com.yannis.baselib.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.yannis.baselib.R
import com.yannis.baselib.utils.net_status.NetStatus
import com.yannis.baselib.utils.net_status.NetStatusChange
import com.yannis.baselib.utils.net_status.NetStatusManager
import com.yannis.baselib.utils.net_status.NetStatusUtils

/**
 * BaseFragment Fragment基类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseFragment<VM : ViewModel, VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB
    lateinit var viewModel: VM
    var mActivity: Activity? = null
    lateinit var skeletonScreen: SkeletonScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initBinding(inflater, container)
        mActivity?.let {
            NetStatusManager.getInstance(it.application).register(this)
            val netType = NetStatusManager.getInstance(it.application).getNetType()
            initAppNetStatus(netType)
        }

        initView()
        refreshData()
        loadData()
        return binding.root
    }

    /**
     * 默认动画显示
     * @param recyclerView recyclerView
     * @param adapter adapter
     * @param layout 骨架layout(即：item view)
     */
    open fun showRecycleSkeleton(
        recyclerView: RecyclerView?,
        adapter: RecyclerView.Adapter<*>?,
        layout: Int
    ) {
        skeletonScreen =
                //绑定当前列表
            Skeleton.bind(recyclerView)
                //设置加载列表适配器 ，并且开启动画 设置光晕动画角度等 最后显示
                .adapter(adapter).shimmer(true).angle(0)
                .frozen(false)
                .duration(1200)
                .count(5)
                .color(R.color.maya_skeleton_color_shimmer)
                .load(layout)
                .show()
    }

    open fun hideSkeleton() {
        skeletonScreen.hide()
    }

    private fun initAppNetStatus(netType: String) {
        Log.e("NetManager", "type is : $netType")
        if (netType != NetStatus.NONE) {
            Log.e("NetManager", "初始：网络已经连接")
        }
        mActivity?.let {
            if (netType === NetStatus.WIFI) {
                if (NetStatusUtils.is5GWifiConnected(it)) {
                    Log.e("NetManager", "这是5G WI-FI")
                } else {
                    Log.e("NetManager", "这是2.4G WI-FI")
                }
                Log.e("NetManager", "WI-FI名：${NetStatusUtils.getConnectedWifiSSID(it)}")
            }
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
            mActivity?.let {
                if (NetStatusUtils.is5GWifiConnected(it)) {
                    Log.e("NetManager", "这是5G WI-FI")
                } else {
                    Log.e("NetManager", "这是2.4G WI-FI")
                }
                Log.e("NetManager", "WI-FI名：${NetStatusUtils.getConnectedWifiSSID(it)}")
            }
        } else if (status == NetStatus.CELLULAR) {
            Log.e("NetManager", "这是 蜂窝网络")
        } else if (status == NetStatus.NET_UNKNOWN) {
            Log.e("NetManager", "这是 未知网络")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity?.let {
            NetStatusManager.getInstance(it.application).unRegister(this)
        }

    }


    open fun refreshData() {}

    abstract fun loadData()

    abstract fun initView()

    private fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        val inflaterLayout = DataBindingUtil.inflate<VDB>(inflater, getLayoutId(), container, false)
        if (ViewModel::class.java != setBindViewModel()) {
            viewModel = ViewModelProvider(this)[setBindViewModel()]
            // -- databinding --
            //inflaterLayout.setVariable(BR.itemMode, viewModel)
            setVariables(inflaterLayout)
            inflaterLayout.lifecycleOwner = this
            // -- databinding --
        }
        binding = inflaterLayout as VDB
    }

    fun setVariables(inflaterLayout: VDB) {}

    abstract fun setBindViewModel(): Class<VM>

    abstract fun getLayoutId(): Int
}