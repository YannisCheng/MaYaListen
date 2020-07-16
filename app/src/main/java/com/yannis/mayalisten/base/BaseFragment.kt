package com.yannis.mayalisten.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yannis.mayalisten.BR

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseFragment<VM : ViewModel, VDB : ViewDataBinding> : Fragment() {

    lateinit var binding: VDB
    lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initBinding()
        initView()
        return binding.root
    }

    abstract fun initView()

    private fun initBinding() {
        val inflater = DataBindingUtil.inflate<VDB>(layoutInflater, getLayoutId(), null, false)
        if (ViewModel::class.java != setBindViewModel()) {
            viewModel = ViewModelProvider(this)[setBindViewModel()]
            // -- databinding --
            inflater.setVariable(BR.itemMode, viewModel)
            inflater.lifecycleOwner = this
            // -- databinding --
        }
        binding = inflater as VDB
    }

    abstract fun setBindViewModel(): Class<VM>

    abstract fun getLayoutId(): Int
}