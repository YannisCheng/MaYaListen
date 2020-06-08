package com.yannis.mayalisten.base

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseActivity : AppCompatActivity() {
    var compositeDisposable: CompositeDisposable? = null


    fun addDispose(disposable: Disposable) {
        compositeDisposable?.add(disposable) ?: CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.let {
            compositeDisposable?.clear()
        }
    }
}