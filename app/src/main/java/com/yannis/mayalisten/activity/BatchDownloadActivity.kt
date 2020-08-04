package com.yannis.mayalisten.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseActivity
import com.yannis.mayalisten.R
import com.yannis.mayalisten.databinding.ActivityBatchDownloadBinding

/**
 * BatchDownloadActivity  批量下载界面
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/8/4 - 15:01
 */
class BatchDownloadActivity : BaseActivity<ViewModel, ActivityBatchDownloadBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, BatchDownloadActivity::class.java)
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

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_batch_download
    }
}