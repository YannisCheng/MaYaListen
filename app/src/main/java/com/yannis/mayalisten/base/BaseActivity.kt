package com.yannis.mayalisten.base

import androidx.appcompat.app.AppCompatActivity
import com.yannis.mayalisten.widget.LoadingDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
abstract class BaseActivity : AppCompatActivity() {
    var compositeDisposable: CompositeDisposable? = null
    val builder: LoadingDialog.Builder = LoadingDialog.Builder(this)

    fun showLoading(msg: String) {
        val create = LoadingDialog
            .Builder(this)
            .setMsg(msg)
            .isCancelOutSide(false)
            .isCancelable(false)
            .create()
        create.show()
    }

    fun hidingLoding() {

    }

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