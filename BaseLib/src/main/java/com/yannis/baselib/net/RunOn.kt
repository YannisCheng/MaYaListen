package com.yannis.baselib.net

import com.yannis.baselib.base.OperatorErrorNextFunc
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 封装RxJava中的"中间数据处理"
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class RunOn<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            // 统一处理 网络请求中的异常
            .onErrorResumeNext(OperatorErrorNextFunc<T>())
            // 处理线程间的切换
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}