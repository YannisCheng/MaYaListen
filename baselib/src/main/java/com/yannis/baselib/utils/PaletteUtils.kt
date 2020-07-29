package com.yannis.baselib.utils

import android.app.Activity
import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import com.yannis.baselib.R
import com.yannis.baselib.utils.status_bar.BarStatusAndStyleUtils

/**
 * PaletteUtils 调色板工具类
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
                    resultCallBack.paletteSuccess(palette)
                }
                // TODO 2020-07-29 17:31:32 待继续做
                palette?.let {
                    val vibrantSwatch = it.vibrantSwatch
                    vibrantSwatch?.let {
                        BarStatusAndStyleUtils.setStatusBarColor(activity, vibrantSwatch.rgb)
                        BarStatusAndStyleUtils.setStatusBarDarkTheme(activity, false)

                    }
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