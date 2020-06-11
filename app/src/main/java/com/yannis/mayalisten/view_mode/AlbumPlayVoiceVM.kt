package com.yannis.mayalisten.view_mode

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.bean.AlbumPlayVoiceBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class AlbumPlayVoiceVM : ViewModel() {

    private var albumPlayVoiceBean: MutableLiveData<AlbumPlayVoiceBean> = MutableLiveData()

    fun getAlbumPlayVoice(trackId: Int): MutableLiveData<AlbumPlayVoiceBean> {

        RetrofitManager.getInstance().getApi()
            .getAlbumPlayEntry(trackId)
            .compose(RunOn<AlbumPlayVoiceBean>())
            .subscribe(object : Observer<AlbumPlayVoiceBean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: AlbumPlayVoiceBean) {
                    albumPlayVoiceBean.value = t
                }

                override fun onError(e: Throwable) {

                }

            })
        return albumPlayVoiceBean
    }
}