package com.yannis.mayalisten.view_mode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.SingleAlbumContentBean
import com.yannis.mayalisten.net.MaYaApi
import com.yannis.mayalisten.net.RetrofitManager2
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * SingleAlbumContentViewModel 但张专辑内容 VM
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 13:51
 */
class SingleAlbumContentViewModel : ViewModel() {

    var mutableLiveData: MutableLiveData<SingleAlbumContentBean> = MutableLiveData()

    fun getSingleAlbumContent(
        albumId: Int,
        asc: Boolean,
        trackId: Int
    ) {
        RetrofitManager2.getInstance().getApi(MaYaApi::class.java)
            .getSingleAlbumContent(albumId, asc, trackId)
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
    }
}