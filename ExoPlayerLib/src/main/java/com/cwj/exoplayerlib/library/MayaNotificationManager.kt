package com.cwj.exoplayerlib.library

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.cwj.exoplayerlib.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * MayaNotificationManager
 *
 * ExoPlayer的PlayerNotificationManager的包装器类。
 * 它设置在音频播放期间向用户显示的通知，并提供曲目元数据，例如曲目标题和图标图像
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
const val NOW_PLAYING_CHANNEL_ID = "com.cwj.exoplayerlib.library.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0xb339
const val NOTIFICATION_LARGE_ICON_SIZE = 144

class MayaNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    private val serviceJob = SupervisorJob()
    private val serverScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            NOW_PLAYING_CHANNEL_ID,
            R.string.notification_channel,
            R.string.notification_channel_description,
            NOW_PLAYING_NOTIFICATION_ID,
            DescriptionAdapter(mediaController),
            notificationListener
        ).apply {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_notification)
            // 不要显示快退或快进按钮。
            setFastForwardIncrementMs(0)
            setRewindIncrementMs(0)
        }
    }

    private inner class DescriptionAdapter(private val mediaController: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {
        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            TODO("Not yet implemented")
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            TODO("Not yet implemented")
        }

        override fun getCurrentContentTitle(player: Player): CharSequence {
            TODO("Not yet implemented")
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            TODO("Not yet implemented")
        }

    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        notificationManager.setPlayer(player)
    }

}