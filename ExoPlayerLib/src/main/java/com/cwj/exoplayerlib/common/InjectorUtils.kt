package com.cwj.exoplayerlib.common

import android.content.ComponentName
import android.content.Context
import com.cwj.exoplayerlib.model.MediaRootViewModel
import com.cwj.exoplayerlib.server.MusicService

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
object InjectorUtils {

    /**
     * 获取 MediaBrowserServiceCompat 单例
     */
    private fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
        return MusicServiceConnection.getInstance(
            context,
            ComponentName(context, MusicService::class.java)
        )
    }

    /**
     * 获取 MediaRootViewModel 单例
     */
    fun provideMediaRootViewModel(context: Context): MediaRootViewModel.Factory {
        val applicationContext = context.applicationContext
        val provideMusicServiceConnection = provideMusicServiceConnection(applicationContext)
        return MediaRootViewModel.Factory(provideMusicServiceConnection)
    }
}