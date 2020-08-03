package com.yannis.baselib.widget

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

    private lateinit var binding: PageStatusLayoutBinding

    init {
        binding = PageStatusLayoutBinding.inflate(LayoutInflater.from(context))
    }

    /**
     * 设置网络异常图片
     */
    private fun setNetErrorImg() {
        showText()
        binding.ivStatus.setImageResource(R.drawable.live_ic_no_network_penguin_top)
        setShowText("网络异常")
    }

    /**
     * 设置服务异常图片
     */
    private fun setServerErrorImg() {
        showText()
        binding.ivStatus.setImageResource(R.drawable.live_ic_no_net)
        setShowText("服务异常")
    }

    /**
     * 设置主题加载loading
     */
    private fun setLoadingTheme() {
        binding.progressTheme.indeterminateDrawable =
            context.getDrawable(R.drawable.host_common_theme_loading)
        hiddenShowText()
    }

    /**
     * 设置普通加载loading
     */
    private fun setLoadingOrdinary() {
        hiddenShowText()
    }

    /**
     * 设置需要显示的提示文字
     */
    private fun setShowText(showText: String) {
        binding.tvShowMsg.text = showText
    }

    private fun hiddenShowText() {
        binding.tvShowMsg.visibility = View.GONE
    }

    private fun showText() {
        binding.tvShowMsg.visibility = View.VISIBLE
    }
}