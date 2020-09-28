package com.yannis.baselib.base

import com.yannis.baselib.net.ExceptionEngine
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * OperatorErrorNextFunct 为onErrorResumeNext()提供参数，统一处理网络请求过程中的异常处理提示。
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/5
 */
class OperatorErrorNextFunc<T> : Function<Throwable, ObservableSource<T>> {
    override fun apply(t: Throwable): ObservableSource<T> {
        return Observable.error(ExceptionEngine.handleException(t))
    }
}