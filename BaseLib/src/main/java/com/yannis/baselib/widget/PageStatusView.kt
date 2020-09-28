package com.yannis.baselib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.yannis.baselib.R
import com.yannis.baselib.databinding.PageStatusLayoutBinding

/**
 * PageStatusView 页面在不同状态下的展示
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/3
 */
class PageStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var clickCallBack: ClickCallBack

    // 在自定义view中使用ViewBinding时，需要在inflate()中设置this、true。否则自定义view不会显示。
    private var binding: PageStatusLayoutBinding =
        PageStatusLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.PageStatusView)
        val color =
            typedArray.getColor(
                R.styleable.PageStatusView_psv_background,
                context.resources.getColor(R.color.maya_color_white)
            )
        binding.rlLyout.setBackgroundColor(color)
        typedArray.recycle()
        binding.tvShowMsg.setOnClickListener { v -> { clickCallBack.tvClickListener() } }
    }

    /**
     * 设置网络异常图片
     */
    fun netErrorImg() {
        binding.ivStatus.setImageResource(R.drawable.live_ic_no_network_penguin_top)
        binding.ivStatus.visibility = View.VISIBLE
        showText("网络异常")
    }

    /**
     * 设置服务异常图片
     */
    fun serverErrorImg() {
        binding.ivStatus.setImageResource(R.drawable.live_ic_no_net)
        binding.ivStatus.visibility = View.VISIBLE
        showText("服务异常")
    }

    /**
     * 隐藏ImageView
     */
    fun hidingImage() {
        binding.ivStatus.visibility = View.GONE
        binding.tvShowMsg.visibility = View.GONE
    }

    /**
     * 设置主题加载loading
     */
    @SuppressLint("ResourceType")
    fun loadingTheme() {
        binding.progressTheme.visibility = View.VISIBLE
    }

    /**
     * 设置普通加载loading
     */
    fun loadingWhite() {
        binding.progressTheme.indeterminateDrawable =
            context.getDrawable(R.drawable.loading_common_white)
        binding.progressTheme.visibility = View.VISIBLE
    }

    /**
     * 隐藏加载loading
     */
    fun hidingLoading() {
        binding.progressTheme.visibility = View.GONE
    }

    /**
     * 设置需要显示的提示文字
     */
    private fun showText(showText: String) {
        binding.tvShowMsg.text = showText
        binding.tvShowMsg.visibility = View.VISIBLE
    }

    fun setTvCallListener(mClickCallBack: ClickCallBack) {
        this.clickCallBack = mClickCallBack
    }

    interface ClickCallBack {
        fun tvClickListener()
    }
}