package com.yannis.baselib.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.baselib.net.RequestThrowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseViewMode ViewMode基类，管理RxJava种的Disposable
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/4 21:55
 */
open class BaseViewMode : ViewModel() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var throwable: MutableLiveData<RequestThrowable> = MutableLiveData()

    fun addDispose(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}