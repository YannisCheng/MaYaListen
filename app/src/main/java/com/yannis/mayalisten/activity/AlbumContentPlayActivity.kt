package com.yannis.mayalisten.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.Gravity
import android.widget.PopupWindow
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yannis.baselib.base.BaseActivity
import com.yannis.baselib.utils.status_bar.BarStatusAndStyleUtils
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.ActivityAlbumContentPlayBinding
import com.yannis.mayalisten.view_mode.AlbumPlayVoiceVM
import com.yannis.mayalisten.widget.PlayListPopupWindow
import com.yannis.mayalisten.widget.TimeClosePopupWindow


private const val BEAN = "track_id"
private const val BEANS = "beans"

/**
 * AlbumContentPlayActivity 专辑->内容->播放界面
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 14:16
 */
class AlbumContentPlayActivity : BaseActivity<AlbumPlayVoiceVM, ActivityAlbumContentPlayBinding>() {

    private lateinit var albumItemBean: AlbumItemBean;
    private lateinit var beans: ArrayList<AlbumItemBean>
    private lateinit var popupWindow: PopupWindow

    companion object {
        @JvmStatic
        fun starter(
            context: Context,
            trackId: AlbumItemBean,
            itemBeans: ArrayList<AlbumItemBean>
        ) {
            val intent = Intent(context, AlbumContentPlayActivity::class.java)
            intent.putExtra(BEAN, trackId)
            intent.putExtra(BEANS, itemBeans)
            context.startActivity(intent)
        }
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

    override fun getLayoutId(): Int {
        return R.layout.activity_album_content_play
    }

    override fun setBindViewModel(): Class<AlbumPlayVoiceVM> {
        return AlbumPlayVoiceVM::class.java
    }

    override fun loadData() {
        viewModel.getAlbumPlayVoice(albumItemBean.trackId)
    }

    override fun dataToView() {
        viewModel.albumPlayVoiceBean.observe(this, Observer {
            binding.apply {
                tvTitle.text = it.trackInfo.title
                val submit = Glide.with(this@AlbumContentPlayActivity).asBitmap()
                    .load(it.trackInfo.images[0]).submit()
                Glide.with(this@AlbumContentPlayActivity).asBitmap()
                    .load(it.trackInfo.images[0]).addListener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val builder: Palette.Builder? =
                                resource?.let { it1 -> Palette.from(it1) }
                            if (builder != null) {
                                builder.generate(object : Palette.PaletteAsyncListener {
                                    override fun onGenerated(palette: Palette?) {
                                        palette?.let {
                                            val vibrantSwatch = it.vibrantSwatch
                                            vibrantSwatch?.let {
                                                BarStatusAndStyleUtils.setStatusBarColor(
                                                    this@AlbumContentPlayActivity,
                                                    vibrantSwatch.rgb
                                                )
                                                BarStatusAndStyleUtils.setStatusBarDarkTheme(
                                                    this@AlbumContentPlayActivity,
                                                    false
                                                )

                                            }
                                        }
                                    }

                                })

                            }
                            return true
                        }

                    }).into(ivCover)

                ivPreviousOne.setOnClickListener {
                    // 上一曲
                }

                ivNextOne.setOnClickListener {
                    // 下一曲
                    Toast.makeText(this@AlbumContentPlayActivity, "下一曲", Toast.LENGTH_SHORT)
                }

                ivPlayStatus.setOnClickListener {
                    // 播放状态
                }

                llPlayList.setOnClickListener {
                    // 播放列表
                    popupWindow = PlayListPopupWindow(this@AlbumContentPlayActivity, beans)
                    popupWindow.showAtLocation(ivTimeClose, Gravity.BOTTOM, 0, 0)
                }

                llTimeClose.setOnClickListener {
                    // 定时关闭
                    popupWindow =
                        TimeClosePopupWindow(this@AlbumContentPlayActivity)
                    popupWindow.showAtLocation(ivTimeClose, Gravity.BOTTOM, 0, 0)
                }
            }
        })
    }

    override fun initView() {
        intent?.let {
            albumItemBean = it.getSerializableExtra(BEAN) as AlbumItemBean
            beans = it.getSerializableExtra(BEANS) as ArrayList<AlbumItemBean>
        }
    }
}