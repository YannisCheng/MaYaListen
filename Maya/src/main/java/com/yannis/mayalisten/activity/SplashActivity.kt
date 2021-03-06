package com.yannis.mayalisten.activity

import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseActivity
import com.yannis.mayalisten.R
import com.yannis.mayalisten.databinding.ActivitySplashBinding

/**
 * SplashActivity  全屏欢迎页
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/30 - 11:19
 */
class SplashActivity : BaseActivity<ViewModel, ActivitySplashBinding>() {

    private lateinit var countDownTimer: CountDownTimer
    override fun dataToView() {

    }

    override fun setBindViewModel(): Class<ViewModel> {
        return ViewModel::class.java
    }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun permissionOk() {
        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.fullscreenContent.text = "跳过广告(${(millisUntilFinished / 1000) + 1})"
            }

        }
        countDownTimer.start()
        binding.fullscreenContent.setOnClickListener {
            countDownTimer.cancel()
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }


}