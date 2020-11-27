package com.cwj.exoplayerlib.server

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.cwj.exoplayerlib.R
import com.cwj.exoplayerlib.common.MayaNotificationManager
import com.cwj.exoplayerlib.common.PersistentStorage
import com.cwj.exoplayerlib.extensions.*
import com.cwj.exoplayerlib.source.BrowseTree
import com.cwj.exoplayerlib.source.MEDIA_SEARCH_SUPPORTED
import com.cwj.exoplayerlib.source.UAMP_BROWSABLE_ROOT
import com.cwj.exoplayerlib.source.UAMP_RECENT_ROOT
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.yannis.mayalisten.audio.JsonSource
import com.yannis.mayalisten.audio.MusicSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * MusicService 音乐浏览器服务端
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
private const val TAG = "MusicService"

class MusicService : MediaBrowserServiceCompat() {

    // - - - - 音乐source获取 - - - -
    private lateinit var musicSource: MusicSource
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val remoteJsonSource: Uri =
        Uri.parse("https://storage.googleapis.com/uamp/catalog.json")

    // - - - - MediaSession
    private lateinit var mediaSession: MediaSessionCompat

    // - - - - 本地存储
    private lateinit var storage: PersistentStorage

    // private lateinit var packageValidator: PackageValidator

    private lateinit var mayaNotificationManager: MayaNotificationManager

    private lateinit var mediaSessionConnector: MediaSessionConnector

    private var currentPlaylistItems = emptyList<MediaMetadataCompat>()


    // - - - - ExoPlayer播放器设置
    // 音频属性
    private val mayaAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    // 播放器事件监听
    private val playerListener = PlayerEventListener()

    // 当前播放器将是ExoPlayer（用于本地播放）或CastPlayer（用于通过Cast设备进行远程播放）。
    //private lateinit var currentPlayer: Player
    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(this).build().apply {
            setAudioAttributes(mayaAudioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerListener)
        }
    }

    private val dataSourceFactory: DefaultDataSourceFactory by lazy {
        DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, UAMP_USER_AGENT),
            null
        )
    }

    private val browseTree: BrowseTree by lazy {
        BrowseTree(applicationContext, musicSource)
    }

    private var isForegroundService = false


    // - - - - 方法 - - - - -
    override fun onCreate() {
        super.onCreate()

        // 媒体库是从远程JSON文件构建的。 我们将在此处创建source，然后使用kotlin协程的suspend功能从主线程执行下载。
        musicSource = JsonSource(sourceUri = remoteJsonSource)
        serviceScope.launch {
            musicSource.loadSource()
        }

        // 构建可用于启动UI的PendingIntent
        val sessionActivityPendingIntent =
            packageManager.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }
        mediaSession = MediaSessionCompat(this, "MusicService").apply {
            setSessionActivity(sessionActivityPendingIntent)
            // 激活状态
            isActive = true
        }
        // 设置sessionToken，经#connect()触发[MediaBrowserCompat.ConnectionCallback()#onConnected()]
        sessionToken = mediaSession.sessionToken
        Log.e(TAG, "onCreate: sessionToken ok")

        storage = PersistentStorage.getInstance(applicationContext)

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlaybackPreparer(MayaPlaybackPreparer())
        mediaSessionConnector.setQueueNavigator(UampQueueNavigator(mediaSession))

        // 切换播放器
        // switchToPlayer()

        // packageValidator = PackageValidator(this,R.xml.allowed_media_browser_callers)
        mayaNotificationManager = MayaNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )
        mayaNotificationManager.showNotificationForPlayer(exoPlayer)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (parentId == UAMP_RECENT_ROOT) {
            result.sendResult(
                storage.loadRecentSong()
                    ?.let { song -> listOf(song) } as MutableList<MediaBrowserCompat.MediaItem>?)
        } else {

            val resultsSent = musicSource.whenReady { successfullyInitialized ->
                if (successfullyInitialized) {
                    val children = browseTree[parentId]?.map { item ->
                        MediaBrowserCompat.MediaItem(item.description, item.flag)
                    }
                    result.sendResult(children as MutableList<MediaBrowserCompat.MediaItem>?)
                } else {
                    mediaSession.sendSessionEvent(NETWORK_FAILURE, null)
                    result.sendResult(null)
                }
            }

            if (!resultsSent) {
                result.detach()
            }
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        val rootExtras = Bundle().apply {
            putBoolean(
                MEDIA_SEARCH_SUPPORTED,
                browseTree.searchableByUnknownCaller
            )
            putBoolean(CONTENT_STYLE_SUPPORTED, true)
            putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID)
            putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST)
        }
        val isRecentRequest = rootHints?.getBoolean(BrowserRoot.EXTRA_RECENT) ?: false
        val browserRootPath = if (isRecentRequest) UAMP_RECENT_ROOT else UAMP_BROWSABLE_ROOT
        return BrowserRoot(browserRootPath, rootExtras)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.apply {
            isActive = false
            release()
        }
        serviceJob.cancel()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

    // - - - - 内部类 - - - -

    private inner class UampQueueNavigator(
        mediaSession: MediaSessionCompat
    ) : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat =
            currentPlaylistItems[windowIndex].description
    }

    /**
     * 通知监听器
     */
    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MusicService.javaClass)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    /**
     * 播放器事件监听
     */
    private inner class PlayerEventListener : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            when (playbackState) {
                Player.STATE_BUFFERING,
                Player.STATE_ENDED -> {
                    mayaNotificationManager.showNotificationForPlayer(exoPlayer)
                    if (playbackState == Player.STATE_READY) {

                        // 播放/暂停时，将当前媒体项目保存在永久存储中，以便可以在设备重新启动之间恢复播放。
                        // 搜索“媒体恢复”以获取更多信息。
                        saveRecentSongToStorage()

                        if (!playWhenReady) {
                            // 如果播放暂停，我们将删除前台状态，该状态允许取消通知。
                            // 另一种选择是在通知中提供一个“关闭”按钮，以停止播放并清除通知。
                            stopForeground(false)
                        }
                    }
                }
                else -> {
                    mayaNotificationManager.hideNotification()
                }
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            var message = R.string.generic_error;
            super.onPlayerError(error)

            when (error.type) {
                // 如果无法加载MediaSource对象中的数据，则Exoplayer会引发type_source错误。
                // 通过Toast消息将错误消息打印到UI，以通知用户。
                ExoPlaybackException.TYPE_SOURCE -> {
                    message = R.string.error_media_not_found;
                    Log.e(TAG, "TYPE_SOURCE: " + error.sourceException.message)
                }
                // 如果在渲染组件中发生错误，则Exoplayer会引发type_remote错误。
                ExoPlaybackException.TYPE_RENDERER -> {
                    Log.e(TAG, "TYPE_RENDERER: " + error.rendererException.message)
                }
                // 如果发生意外的RuntimeException，则Exoplayer会引发type_unexpected错误。
                ExoPlaybackException.TYPE_UNEXPECTED -> {
                    Log.e(TAG, "TYPE_UNEXPECTED: " + error.unexpectedException.message)
                }
                // 当发生OutOfMemory错误时发生。
                ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
                    Log.e(TAG, "TYPE_OUT_OF_MEMORY: " + error.outOfMemoryError.message)
                }
                // 如果错误发生在远程组件中，则Exoplayer会引发type_remote错误。
                ExoPlaybackException.TYPE_REMOTE -> {
                    Log.e(TAG, "TYPE_REMOTE: " + error.message)
                }
            }
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveRecentSongToStorage() {
        // 在将当前歌曲详细信息保存到单独的线程之前，请获取它们的详细信息，
        // 否则在保存例程运行时可能已卸载了当前播放器。
        val description = currentPlaylistItems[exoPlayer.currentWindowIndex].description
        val currentPosition = exoPlayer.currentPosition
        serviceScope.launch {
            storage.saveRecentSong(description, currentPosition)
        }
    }

    /**
     * ExoPlayer的播放准备器
     */
    private inner class MayaPlaybackPreparer : MediaSessionConnector.PlaybackPreparer {
        override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) =
            Unit

        override fun onCommand(
            player: Player,
            controlDispatcher: ControlDispatcher,
            command: String,
            extras: Bundle?,
            cb: ResultReceiver?
        ): Boolean = false

        override fun getSupportedPrepareActions(): Long =
            PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH

        override fun onPrepareFromMediaId(
            mediaId: String,
            playWhenReady: Boolean,
            extras: Bundle?
        ) {
            musicSource.whenReady {
                val itemToPlay: MediaMetadataCompat? = musicSource.find {
                    it.id == mediaId
                }

                if (itemToPlay == null) {
                    Log.e(TAG, "Content not found: MediaID=$mediaId")
                } else {

                    val playbackStartPositionMs = extras?.getLong(
                        MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, C.TIME_UNSET
                    ) ?: C.TIME_UNSET
                    preparePlaylist(
                        buildPlaylist(itemToPlay),
                        itemToPlay,
                        playWhenReady,
                        playbackStartPositionMs
                    )
                }
            }
        }

        override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

        override fun onPrepare(playWhenReady: Boolean) {
            val recentSong = storage.loadRecentSong() ?: return
            onPrepareFromMediaId(
                recentSong.mediaId!!,
                playWhenReady,
                recentSong.description.extras
            )
        }
    }

    private fun buildPlaylist(itemToPlay: MediaMetadataCompat): List<MediaMetadataCompat> =
        musicSource.filter {
            it.album == itemToPlay.album
        }.sortedBy {
            it.trackNumber
        }


    /**
     * 加载提供的歌曲列表，然后将歌曲播放到当前播放器中。
     */
    private fun preparePlaylist(
        metadataList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playWhenReady: Boolean,
        playbackStartPositionMs: Long
    ) {
        // 由于播放列表可能是基于某种顺序的（例如专辑中的曲目），
        // 因此请找到首先播放哪个窗口索引，以便用户实际上想播放的歌曲首先播放。
        val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
        currentPlaylistItems = metadataList
        // 简化版播放器处理
        exoPlayer.playWhenReady = playWhenReady
        exoPlayer.stop(true)
        val mediaSource = metadataList.toMediaSource(dataSourceFactory)
        exoPlayer.prepare(mediaSource)
        exoPlayer.seekTo(initialWindowIndex, playbackStartPositionMs)
    }
}

/*
 * (Media) Session events
 */
const val NETWORK_FAILURE = "com.example.android.uamp.media.session.NETWORK_FAILURE"

/** Content styling constants */
private const val CONTENT_STYLE_BROWSABLE_HINT = "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT"
private const val CONTENT_STYLE_PLAYABLE_HINT = "android.media.browse.CONTENT_STYLE_PLAYABLE_HINT"
private const val CONTENT_STYLE_SUPPORTED = "android.media.browse.CONTENT_STYLE_SUPPORTED"
private const val CONTENT_STYLE_LIST = 1
private const val CONTENT_STYLE_GRID = 2

private const val UAMP_USER_AGENT = "uamp.next"

val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"