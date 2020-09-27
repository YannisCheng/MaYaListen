package com.yannis.mayalisten.activity

import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseActivity
import com.yannis.mayalisten.R
import com.yannis.mayalisten.databinding.ActivityTestCaseBinding

/**
 * TestCaseActivity.kt  展示各种View及工具的测试效果Activity
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/8/4 - 14:11
 */
class TestCaseActivity : BaseActivity<ViewModel, ActivityTestCaseBinding>() {

    override fun dataToView() {

    }

    override fun setBindViewModel(): Class<ViewModel> {
        return ViewModel::class.java
    }

    override fun initView() {
        //showLoading("")
        //binding.psv.loadingTheme()
        binding.psv.serverErrorImg()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_case
    }

    override fun permissionOk() {
        TODO("Not yet implemented")
    }
}