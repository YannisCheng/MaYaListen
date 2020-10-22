package com.yannis.baselib.utils

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.google.android.flexbox.FlexboxLayout
import com.yannis.baselib.R

/**
 * FlexBoxLayout 创建item
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/10/22
 */
class FlexBoxLayoutItem private constructor() {

    /**
     * 单例-静态内部类模式
     */
    companion object {
        val instance = Holder.holder
    }

    private object Holder {
        val holder = FlexBoxLayoutItem()
    }


    fun create(text: String, context: Context): TextView {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.textSize = 11f
        textView.setTextColor(context.resources.getColor(R.color.maya_color_theme))
        textView.setBackgroundResource(R.drawable.cstudy_tag_bg)

        ViewCompat.setPaddingRelative(textView, dp2px(8f), dp2px(3f), dp2px(8f), dp2px(3f))

        val layoutParams = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(0, dp2px(3f), dp2px(6f), dp2px(3f))

        textView.layoutParams = layoutParams

        return textView
    }
}