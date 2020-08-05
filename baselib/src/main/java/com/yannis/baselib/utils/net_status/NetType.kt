package com.yannis.baselib.utils.net_status

import androidx.annotation.StringDef

/**
 * 注解代替枚举
 * 参考：https://blog.csdn.net/sinat_31057219/article/details/103912499
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/5
 */
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
@StringDef()
@MustBeDocumented
annotation class NetType {
    companion object {
        const val WIFI = "WIFI"
        const val CELLULAR = "CELLULAR"
        const val NET_UNKNOWN = "NET_UNKNOWN"
    }
}