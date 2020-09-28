package com.yannis.baselib.utils

import android.app.Activity
import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import com.yannis.baselib.R
import com.yannis.baselib.utils.status_bar.StatusBarUtils

/**
 * PaletteUtils 调色板工具类
 *
 * 参考：https://www.jianshu.com/p/450c89bf9d38
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
class PaletteUtils(bitmap: Bitmap, private val activity: Activity) {

    private lateinit var resultCallBack: PaletteResultCallBack

    init {
        val builder: Palette.Builder = Palette.from(bitmap)
        builder.generate(object : Palette.PaletteAsyncListener {
            override fun onGenerated(palette: Palette?) {
                if (palette == null) {
                    resultCallBack.paletteError(activity.resources.getColor(R.color.maya_color_white))
                } else {
                    val vibrantSwatch = palette.vibrantSwatch
                    vibrantSwatch?.let {
                        StatusBarUtils.setStatusBarColor(activity, vibrantSwatch.rgb)
                        StatusBarUtils.setStatusBarDarkTheme(activity, false)

                    }
                    resultCallBack.paletteSuccess(palette)
                }
            }
        })
    }

    fun setCallBackListener(callBack: PaletteResultCallBack) {
        this.resultCallBack = callBack
    }

    /**
     * Palette异步接口回调结果
     */
    interface PaletteResultCallBack {

        /**
         * 颜色获取成功
         */
        fun paletteSuccess(palette: Palette)

        /**
         * 颜色获取失败
         */
        fun paletteError(defColor: Int)
    }
}