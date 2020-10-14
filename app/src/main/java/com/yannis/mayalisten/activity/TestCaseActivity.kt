package com.yannis.mayalisten.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseActivity
import com.yannis.mayalisten.R
import com.yannis.mayalisten.databinding.ActivityTestCaseBinding
import com.yannis.mayalisten.notification.NotificationCompatUtil

/**
 * TestCaseActivity.kt  展示各种View及工具的测试效果Activity
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/8/4 - 14:11
 */
class TestCaseActivity : BaseActivity<ViewModel, ActivityTestCaseBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, TestCaseActivity::class.java)
            //.putExtra()
            context.startActivity(starter)
        }
    }


    override fun dataToView() {

    }

    override fun setBindViewModel(): Class<ViewModel> {
        return ViewModel::class.java
    }

    override fun initView() {
        //showLoading("")
        //binding.psv.loadingTheme()
        //binding.psv.serverErrorImg()

        val notificationCompatUtil =
            NotificationCompatUtil(
                this,
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            )
        // 创建2个不同渠道的通知
        notificationCompatUtil.createNotification()
        // 显示通知
        binding.btnChat.setOnClickListener {
            notificationCompatUtil.sendChatMsg()
        }
        binding.btnSubscribe.setOnClickListener {
            notificationCompatUtil.sendSubscribeMsg()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_case
    }

    override fun permissionOk() {
        TODO("Not yet implemented")
    }
}