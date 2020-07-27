package com.yannis.baselib.utils.status_bar

import androidx.annotation.IntDef

/**
 * StatusBarUtil 状态栏工具类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/27
 */
const val TYPE_MIUI: Int = 0
const val TYPE_FLYME: Int = 1
const val TYPE_M: Int = 3 //6.0

class StatusBarUtil {

    @IntDef(*[TYPE_MIUI, TYPE_FLYME, TYPE_M])
    @Retention(AnnotationRetention.SOURCE)
    @MustBeDocumented
    annotation class ViewType {
    }

    companion object {

    }
}