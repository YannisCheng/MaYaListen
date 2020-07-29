package com.yannis.baselib.utils.glidex

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.yannis.baselib.R


/**
 * Glide 使用封装
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
object GlideX {

    /**
     * 基本配置
     */
    private fun getOptions(): RequestOptions {
        val requestOptions = RequestOptions()
        return requestOptions
            .placeholder(R.drawable.live_default_avatar_88)
            .error(R.drawable.default_ptr_rotate)
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
}