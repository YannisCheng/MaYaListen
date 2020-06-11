package com.yannis.mayalisten.view_mode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.SingleAlbumContentBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class SingleAlbumContentViewModel : ViewModel() {


    fun getSingleAlbumContent(
        albumId: Int,
        asc: Boolean,
        trackId: Int
    ): MutableLiveData<SingleAlbumContentBean> {
        var mutableLiveData: MutableLiveData<SingleAlbumContentBean> = MutableLiveData()

        RetrofitManager.getInstance().getApi().getSingleAlbumContent(albumId, asc, trackId)
            .compose(RunOn<BaseResultBean<SingleAlbumContentBean>>())
            .subscribe(object : Observer<BaseResultBean<SingleAlbumContentBean>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    //addDispose(d)
                }

                override fun onNext(t: BaseResultBean<SingleAlbumContentBean>) {
                    mutableLiveData.value = t.data
                }

                override fun onError(e: Throwable) {
                    //Log.e("TAG", "onError: " + e.toString())
                }
            })

        return mutableLiveData
    }
}