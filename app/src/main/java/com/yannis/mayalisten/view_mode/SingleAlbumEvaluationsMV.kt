package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.AlbumEvaluationsBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * SingleAlbumEvaluationsMV 单张专辑评价VM
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
class SingleAlbumEvaluationsMV : ViewModel() {

    var bean: MutableLiveData<AlbumEvaluationsBean> = MutableLiveData()

    fun getSingleAlbumEvaluations(albumId: Int) {

        RetrofitManager.getInstance().getApi()
            .getAlbumEvaluations(albumId)
            .compose(RunOn<BaseResultBean<AlbumEvaluationsBean>>())
            .subscribe(
                object : Observer<BaseResultBean<AlbumEvaluationsBean>> {
                    override fun onComplete() {
                        Log.e("TAG", "AggregateRankListTabs onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        //addDispose(d)
                    }

                    override fun onNext(t: BaseResultBean<AlbumEvaluationsBean>) {
                        bean.value = t.data
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG", e.toString())
                    }
                })
    }

}