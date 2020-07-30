package com.yannis.baselib.utils.glidex

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.yannis.baselib.R
import com.yannis.baselib.utils.PaletteUtils


/**
 * Glide 使用封装
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
class GlideX private constructor() {

    companion object {
        @JvmStatic
        fun getInstance(): GlideX {
            return SingleHolder.glideX
        }
    }

    /**
     * 静态内部类
     */
    private object SingleHolder {
        val glideX: GlideX = GlideX()
    }

    /**
     * 基本配置
     */
    private fun getOptions(): RequestOptions {
        val requestOptions = RequestOptions()
        return requestOptions
            .placeholder(R.drawable.live_default_avatar_88)
            .error(R.drawable.live_btn_host_panel_photo)
    }

    /**
     * 圆角裁切
     */
    fun rectF(context: Context, url: String, iv: ImageView, radius: Int) {
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()

        Glide.with(context)
            .load(url)
            // 圆角
            .apply(
                getOptions().centerCrop().transform(GlideRectFTransform(context, radius, -1, -1))
            )
            // 渐变动画
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(iv)
    }

    /**
     * 带边框的圆角裁切
     */
    fun rectFWithFrame(
        context: Context,
        url: String,
        iv: ImageView,
        radius: Int,
        frame: Int,
        color: Int
    ) {
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()

        Glide.with(context)
            .load(url)
            // 圆角
            .apply(
                getOptions().centerCrop()
                    .transform(GlideRectFTransform(context, radius, frame, color))
            )
            // 渐变动画
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(iv)
    }

    /**
     * 圆形裁切
     */
    fun roundLoader(context: Context, url: String, iv: ImageView) {

        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()

        Glide.with(context)
            .load(url)
            .apply(getOptions().circleCrop())
            .circleCrop()
            // 渐变动画
            .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(iv)
    }

    /**
     * 带有调色板监听的图片加载方法
     */
    fun loaderWithPalette(
        activity: Activity,
        url: String,
        iv: ImageView,
        adius: Int,
        paletteResultCallBack: PaletteUtils.PaletteResultCallBack
    ) {

        Glide.with(activity)
            .asBitmap()
            .load(url)
            .apply(
                getOptions()
                    .centerCrop()
                //.transform(GlideRectFTransform(activity, radius, -1, -1))
            )
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    iv.setImageDrawable(activity.resources.getDrawable(R.drawable.live_btn_host_panel_photo))
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    iv.setImageBitmap(resource)
                    resource?.let {
                        PaletteUtils(resource, activity).setCallBackListener(paletteResultCallBack)
                    }
                    return true
                }
            })
            .into(iv)
    }
}