package com.yannis.baselib.utils

import android.content.Context
import android.content.res.Configuration

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/24
 */
class ThemeNightUtil {
    companion object {
        /**
         * 判断当前系统是否是深色主题
         */
        fun isDarkTheme(context: Context): Boolean {

            val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            return flag == Configuration.UI_MODE_NIGHT_YES

        }
    }

}