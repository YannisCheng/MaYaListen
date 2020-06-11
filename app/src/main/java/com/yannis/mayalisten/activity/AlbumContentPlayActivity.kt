package com.yannis.mayalisten.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.ActivityAlbumContentPlayBinding
import com.yannis.mayalisten.view_mode.AlbumPlayVoiceVM

/**
 * 专辑->内容->播放界面
 */
private const val BEAN = "track_id"

class AlbumContentPlayActivity : AppCompatActivity() {

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
                }
            }
        })


    }
}