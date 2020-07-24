package com.yannis.mayalisten.view_mode

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.bean.AlbumPlayVoiceBean
import com.yannis.mayalisten.net.MaYaApi
import com.yannis.mayalisten.net.RetrofitManager2
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * AlbumPlayVoiceVM 播放专辑中单个声音 VM
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class AlbumPlayVoiceVM : ViewModel() {

    var albumPlayVoiceBean: MutableLiveData<AlbumPlayVoiceBean> = MutableLiveData()

    fun getAlbumPlayVoice(trackId: Int) {

        RetrofitManager2.getInstance().getApi(MaYaApi::class.java)
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
    }
}