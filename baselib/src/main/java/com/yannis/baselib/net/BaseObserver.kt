package com.yannis.baselib.net

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/4
 */
abstract class BaseObserver<T> : Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        dealNext(t)
    }

    abstract fun dealNext(t: T)

    override fun onError(e: Throwable) {

        desError(ExceptionEngine.handleException(e))
    }

    abstract fun desError(handleException: RequestThrowable)
}


