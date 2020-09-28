package com.yannis.baselib.utils.status_bar

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.yannis.baselib.R

/**
 * StatusBarHeightView: 状态栏高度View,用于沉浸占位
 *
 * 参考：https://www.jianshu.com/p/a8850bae0c66
 * @JvmOverloads：如果你没有加上这个注解，它只能重载相匹配的的构造函数，而不是全部
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
class StatusBarHeightView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    var statusBarHeight: Int = 0
    var type: Int = 0

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (identifier > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(identifier);
            }
        } else {
            //低版本 直接设置0
            statusBarHeight = 0;
        }

        if (context != null) {
            attrs?.let {
                val typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.StatusBarHeightView)
                type = typedArray.getInt(R.styleable.StatusBarHeightView_use_type, 0)
                typedArray.recycle()
            }
        }

        if (type == 1) {
            setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (type == 0) {
            setMeasuredDimension(
                View.getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                statusBarHeight
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}