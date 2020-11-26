package com.cwj.exoplayerlib

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelLazy
import com.cwj.exoplayerlib.common.InjectorUtils
import com.cwj.exoplayerlib.databinding.MediaActivityRootBinding
import com.cwj.exoplayerlib.model.MediaRootViewModel
import com.yannis.baselib.base.BaseActivity

/**
 * 媒体测试播放页
 */
private const val TAG = "MediaRootActivity"
class MediaRootActivity : BaseActivity<MediaRootViewModel, MediaActivityRootBinding>() {

    /*private val viewModelMusic by viewModels<MediaRootViewModel> {
        InjectorUtils.provideMediaRootViewModel(this)
    }*/

    private val viewModelMusic by ViewModelLazy(
        MediaRootViewModel::class,
        { viewModelStore },
        { InjectorUtils.provideMediaRootViewModel(this) }
    )


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

    override fun setBindViewModel(): Class<MediaRootViewModel>? {
        return null
    }

    override fun initView() {
        viewModelMusic.tempCase.observe(this, Observer {
            Log.e(TAG, "initView: ${viewModelMusic.tempCase.value}")
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.media_activity_root
    }

    override fun permissionOk() {

    }
}