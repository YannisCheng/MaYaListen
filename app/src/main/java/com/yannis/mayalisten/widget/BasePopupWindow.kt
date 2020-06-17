package com.yannis.mayalisten.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.PaintDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import androidx.viewbinding.ViewBinding

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
open class BasePopupWindow(mContext: Context) : PopupWindow(),
    PopupWindow.OnDismissListener {

    private var window: Window = (mContext as Activity).window

    fun setBaseDialogSettingNoBg(binding: ViewBinding) {
        apply {
            contentView = binding.root
            isOutsideTouchable = true
            isFocusable = true
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            setBackgroundDrawable(PaintDrawable())
            setOnDismissListener(this@BasePopupWindow)
            update()
        }
    }

    fun setBaseDialogSetting(binding: ViewBinding) {
        setBaseDialogSettingNoBg(binding)
        setWindowAlpha(0.4f)
    }

    fun setWindowAlpha(fl: Float) {
        window.let {
            val lp = it.attributes
            lp.alpha = fl
            it.attributes = lp
        }
    }

    override fun onDismiss() {
        setWindowAlpha(1f)
    }
}