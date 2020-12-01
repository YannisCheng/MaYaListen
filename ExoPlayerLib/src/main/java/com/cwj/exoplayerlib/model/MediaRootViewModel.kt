package com.cwj.exoplayerlib.model

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.cwj.exoplayerlib.common.Event
import com.cwj.exoplayerlib.common.MediaItemData
import com.cwj.exoplayerlib.common.MusicServiceConnection
import com.cwj.exoplayerlib.extensions.id
import com.cwj.exoplayerlib.extensions.isPlayEnabled
import com.cwj.exoplayerlib.extensions.isPlaying
import com.cwj.exoplayerlib.extensions.isPrepared
import com.example.android.uamp.fragments.NowPlayingFragment

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/23
 */
class MediaRootViewModel(private val musicServiceConnection: MusicServiceConnection) : ViewModel() {

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

    val rootMediaId: LiveData<String> =
        Transformations.map(musicServiceConnection.isConnected) { isConnected ->
            if (isConnected) {
                musicServiceConnection.rootMediaId
            } else {
                null
            }
        }

    val navigateToMediaItem: LiveData<Event<String>> get() = _navigateToMediaItem
    private val _navigateToMediaItem = MutableLiveData<Event<String>>()

    val navigateToFragment: LiveData<Event<FragmentNavigationRequest>> get() = _navigateToFragment
    private val _navigateToFragment = MutableLiveData<Event<FragmentNavigationRequest>>()

    fun mediaItemClicked(clickedItem: MediaItemData) {
        if (clickedItem.browsable) {
            browseToItem(clickedItem)
        } else {
            playMedia(clickedItem, pauseAllowed = false)
            showFragment(NowPlayingFragment.newInstance())
        }
    }

    fun playMedia(mediaItem: MediaItemData, pauseAllowed: Boolean = true) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId == nowPlaying?.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying ->
                        if (pauseAllowed) transportControls.pause() else Unit
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.e(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=${mediaItem.mediaId})"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    fun playMediaId(mediaId: String) {
        val nowPlaying = musicServiceConnection.nowPlaying.value
        val transportControls = musicServiceConnection.transportControls

        val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaId == nowPlaying?.id) {
            musicServiceConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(
                            TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                    " (mediaId=$mediaId)"
                        )
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaId, null)
        }
    }

    fun showFragment(fragment: Fragment, backStack: Boolean = true, tag: String? = null) {
        _navigateToFragment.value = Event(FragmentNavigationRequest(fragment, backStack, tag))
    }

    private fun browseToItem(mediaItem: MediaItemData) {
        _navigateToMediaItem.value = Event(mediaItem.mediaId)
    }
}

/**
 * Helper类用于在MainActivity及其对应的ViewModel之间传递片段导航请求
 */
data class FragmentNavigationRequest(
    val fragment: Fragment,
    val backStack: Boolean = false,
    val tag: String? = null
)

private const val TAG = "MediaRootViewModel"