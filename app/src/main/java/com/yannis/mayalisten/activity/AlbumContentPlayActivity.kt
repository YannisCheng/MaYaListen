package com.yannis.mayalisten.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.yannis.mayalisten.base.BaseActivity
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.ActivityAlbumContentPlayBinding
import com.yannis.mayalisten.view_mode.AlbumPlayVoiceVM
import com.yannis.mayalisten.widget.TimeClosePopupWindow

/**
 * 专辑->内容->播放界面
 */
private const val BEAN = "track_id"

class AlbumContentPlayActivity : BaseActivity() {

    private lateinit var binding: ActivityAlbumContentPlayBinding
    private lateinit var albumItemBean: AlbumItemBean;

    companion object {
        @JvmStatic
        fun starter(context: Context, trackId: AlbumItemBean) {
            val intent = Intent(context, AlbumContentPlayActivity::class.java)
            intent.putExtra(BEAN, trackId)
            context.startActivity(intent)
        }
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            albumItemBean = it.getSerializableExtra(BEAN) as AlbumItemBean
        }
        binding = ActivityAlbumContentPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val voiceVM: AlbumPlayVoiceVM by viewModels()

        voiceVM.getAlbumPlayVoice(albumItemBean.trackId).observe(this, Observer {
            binding.apply {
                tvTitle.text = it.trackInfo.title
                Glide.with(this@AlbumContentPlayActivity).load(it.trackInfo.images[0]).into(ivCover)
                ivPreviousOne.setOnClickListener {
                    // 上一曲
                }

                ivNextOne.setOnClickListener {
                    // 下一曲
                }

                ivPlayStatus.setOnClickListener {
                    // 播放状态
                }

                ivPlayList.setOnClickListener {
                    // 播放列表
                }

                ivTimeClose.setOnClickListener {
                    // 定时关闭
                    popupWindow =
                        TimeClosePopupWindow(this@AlbumContentPlayActivity)
                    popupWindow.showAtLocation(ivTimeClose, Gravity.BOTTOM, 0, 0)
                }
            }
        })
    }

    /*override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        popupWindow.takeIf { it.isShowing }?.let { return true }
        return super.dispatchTouchEvent(ev)
    }*/

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (event?.keyCode) {
            KeyEvent.KEYCODE_BACK -> popupWindow.takeIf { it.isShowing }?.dismiss() ?: backClick()
        }
        return super.onKeyDown(keyCode, event)
    }*/

    private fun backClick() {
        finish()
    }
}