package com.yannis.baselib.widget

import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.ScreenUtils


/**
 * TextShrinkExpansion 文本的展开收起
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/27
 */
@RequiresApi(Build.VERSION_CODES.M)
class TextShrinkExpansion(
    private val textView: TextView,
    private val content: String,
    private val lines: Int
) {

    //收起的文字
    private lateinit var shrinkContent: SpannableString

    //展开的文字
    private lateinit var newContent: SpannableString

    init {
        val textPaint: TextPaint = textView.paint
        val withLine: Int = ScreenUtils.getAppScreenWidth()
        val staticLayout: StaticLayout = StaticLayout.Builder
            .obtain(content, 0, content.length, textPaint, withLine)
            .build()

        if (staticLayout.lineCount > 3) {
            // 展开文本对应的内容
            openText()

            // 收起文本对应的内容
            closeText(staticLayout)
        } else {
            textView.text = content
            textView.setOnClickListener(null)
        }
    }

    private fun openText() {
        val contentAdd = "$content    收起"
        newContent = SpannableString(contentAdd)
        newContent.setSpan(
            ForegroundColorSpan(Color.parseColor("#0079e2")),
            contentAdd.length - 2,
            contentAdd.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun closeText(staticLayout: StaticLayout) {
        // 返回文本中的指定行的开头的偏移量
        val index = staticLayout.getLineStart(lines)

        // 设置收起文本的最后一行数据
        val interceptStr: String = "${content.subSequence(0, index - 2)}...更多"
        shrinkContent = SpannableString(interceptStr)
        // 设置收起文本的最后一行数据中"...更多"的颜色
        shrinkContent.setSpan(
            ForegroundColorSpan(Color.parseColor("#0079e2")),
            interceptStr.length - 2,
            interceptStr.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // 展示"收起文本"的内容
        textView.text = shrinkContent
        // 默认设置选中
        textView.isSelected = true

        textView.setOnClickListener {
            if (it.id == textView.id) {
                if (it.isSelected) {
                    //如果是收起的状态
                    textView.setText(newContent);
                    textView.isSelected = false;
                } else {
                    //如果是展开的状态
                    textView.setText(shrinkContent);
                    textView.isSelected = true;
                }
            }
        }
    }

}