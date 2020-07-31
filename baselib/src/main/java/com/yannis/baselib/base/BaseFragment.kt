package com.yannis.baselib.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseFragment<VM : ViewModel, VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB
    lateinit var viewModel: VM
    lateinit var mActivity: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initBinding(inflater, container)
        initView()
        refreshData()
        loadData()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
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