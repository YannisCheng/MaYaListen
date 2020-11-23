package com.cwj.exoplayerlib

import android.content.Context
import android.content.Intent
import com.cwj.exoplayerlib.databinding.MediaActivityRootBinding
import com.cwj.exoplayerlib.model.MediaRootViewModel
import com.yannis.baselib.base.BaseActivity

/**
 * 媒体测试播放页
 */
class MediaRootActivity : BaseActivity<MediaRootViewModel, MediaActivityRootBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MediaRootActivity::class.java)
            //.putExtra()
            context.startActivity(starter)
        }
    }

    override fun dataToView() {

    }

    override fun setBindViewModel(): Class<MediaRootViewModel> {
        return MediaRootViewModel::class.java
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.media_activity_root
    }

    override fun permissionOk() {

    }
}