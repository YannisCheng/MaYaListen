package com.yannis.baselib.net

/**
 * ExceptionEngine 异常统一处理
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/4
 */
class ExceptionEngine(private var eCode: Int = 0, private var eMessage: String = "") : Throwable() {

    fun handleException(e: Throwable) {

    }


}