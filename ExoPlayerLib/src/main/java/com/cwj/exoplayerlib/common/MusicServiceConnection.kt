package com.cwj.exoplayerlib.common

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import com.cwj.exoplayerlib.extensions.id
import com.cwj.exoplayerlib.server.NETWORK_FAILURE

/**
 * MusicServiceConnection 封装"服务层"与"界面展示层"的"中间连接层"
 * 1.处理MediaBrowser与MediaBrowserService的连接状态
 * 2.处理连接成功后MediaController与其回调
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
class MusicServiceConnection(context: Context, serviceComponent: ComponentName) {

    /**
     * 单例获取
     */
    companion object {

        @Volatile
        private var INSTANCE: MusicServiceConnection? = null

        fun getInstance(context: Context, serviceComponent: ComponentName) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MusicServiceConnection(context, serviceComponent)
                    .also { INSTANCE = it }
            }
    }

    // - - - - 变量 - - - -
    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaControllerCallback = MediaControllerCallback()
    private val mediaBrowser = MediaBrowserCompat(
        context,
        serviceComponent,
        mediaBrowserConnectionCallback,
        null
    )
    private lateinit var mediaController: MediaControllerCompat

    // - - - - states setting - - - -
    /**
     * MediaBrowser与MediaBrowserService的连接状态
     */
    val isConnected = MutableLiveData<Boolean>()
        .apply {
            postValue(false)
        }

    /**
     * 播放控制器中返回的的网络失败
     */
    val networkFailure = MutableLiveData<Boolean>()
        .apply {
            postValue(false)
        }

    val rootMediaId: String get() = mediaBrowser.root

    /**
     * 媒体控制器的播放状态，默认为空
     */
    val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply {
            postValue(EMPTY_PLAYBACK_STATE)
        }

    /**
     * 设置播放时"媒体元数据"，停止播放时为null
     */
    val nowPlaying = MutableLiveData<MediaMetadataCompat>()
        .apply {
            postValue(NOTHING_PLAYING)
        }

    /**
     * 用于控制会话中媒体播放的界面。 这允许应用程序将媒体传输命令发送到会话
     */
    val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    // - - - - 方法 - - - -
    /**
     * 封装一层订阅方法，此方法的作用是：从Service中获取已经得到的数据
     */
    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    // - - - - callbacks  - - - -
    private inner class MediaBrowserConnectionCallback(private val context: Context) :
        MediaBrowserCompat.ConnectionCallback() {
        /**
         * 请求成功完成后，在MediaBrowserCompat.connect之后调用。
         */
        override fun onConnected() {
            super.onConnected()
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
                .apply {
                    registerCallback(mediaControllerCallback)
                }
            isConnected.postValue(true)
        }

        /**
         * 与媒体浏览器的连接失败时调用。
         */
        override fun onConnectionFailed() {
            super.onConnectionFailed()
            isConnected.postValue(false)
        }

        /**
         * 当客户端与媒体浏览器断开连接时调用
         */
        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            isConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            // 当ExoPlayer停止时，我们将收到带有“空”元数据的回调。 这是一个已使用默认值实例化的元数据对象。
            // 媒体ID的默认值为null，因此我们假定如果该值为null，则我们不会播放任何内容。
            nowPlaying.postValue(
                if (metadata?.id == null) {
                    NOTHING_PLAYING
                } else {
                    metadata
                }
            )
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_FAILURE -> networkFailure.postValue(true)
            }
        }

        /**
         * 通常，如果[MediaBrowserServiceCompat]断开其连接，则回调将通过[MediaControllerCompat.Callback]（在此）进行。
         * 但是，由于其他连接状态事件已发送到[MediaBrowserCompat.ConnectionCallback]，
         * 因此我们在此处捕获了断开连接，并将其发送到另一个回调。
         */
        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

/**
 * 初始化：播放器播放状态
 */
val EMPTY_PLAYBACK_STATE = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

/**
 * 初始化：媒体元数据
 */
val NOTHING_PLAYING = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0L)
    .build()