package com.cwj.exoplayerlib.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cwj.exoplayerlib.common.MusicServiceConnection

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/23
 */
class MediaRootViewModel(musicServiceConnection: MusicServiceConnection) : ViewModel() {

    /**
     * ViewModel创建"带有参数"的单实例方式
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(private val musicServiceConnection: MusicServiceConnection) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MediaRootViewModel(musicServiceConnection) as T
        }

    }

    var tempCase: MutableLiveData<String> = MutableLiveData<String>("Test")
}