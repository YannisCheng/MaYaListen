package com.cwj.exoplayerlib.common

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.bumptech.glide.Glide
import com.cwj.exoplayerlib.extensions.asAlbumArtContentUri
import com.cwj.exoplayerlib.server.MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * PersistentStorage 持久化
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
private const val PREFERENCES_NAME = "maya_listen"
private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"
private const val RECENT_SONG_TITLE_KEY = "recent_song_title"
private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"
private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"
private const val RECENT_SONG_POSITION_KEY = "recent_song_position"

class PersistentStorage private constructor(val context: Context) {

    companion object {
        @Volatile
        private var instance: PersistentStorage? = null

        fun getInstance(context: Context): PersistentStorage =
            instance ?: synchronized(this) {
                instance ?: PersistentStorage(context).also { instance = it }
            }

    }

    /**
     * SharedPreferences实例
     */
    private var preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * 存储
     */
    suspend fun saveRecentSong(description: MediaDescriptionCompat, position: Long) {
        withContext(Dispatchers.IO) {
            /**
             * app启动后，Android将尝试为最近播放的歌曲构建静态媒体控件。
             * 这些媒体控件的插图不应该从网络上加载，因为它启动后可能太慢或无法立即使用。
             * 相反，我们将iconUri转换为指向Glide磁盘缓存
             */
            val localIconUri = Glide.with(context).asFile().load(description.iconUri)
                .submit(
                    NOTIFICATION_LARGE_ICON_SIZE,
                    NOTIFICATION_LARGE_ICON_SIZE
                ).get()
                .asAlbumArtContentUri()

            preferences.edit()
                .putString(RECENT_SONG_MEDIA_ID_KEY, description.mediaId)
                .putString(RECENT_SONG_TITLE_KEY, description.title.toString())
                .putString(RECENT_SONG_SUBTITLE_KEY, description.subtitle.toString())
                .putString(RECENT_SONG_ICON_URI_KEY, localIconUri.toString())
                .putLong(RECENT_SONG_POSITION_KEY, position)
                .apply()
        }
    }

    /**
     * 加载
     */
    fun loadRecentSong(): MediaBrowserCompat.MediaItem? {
        val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)

        return if (mediaId == null) {
            null
        } else {
            val extras = Bundle().also {
                val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
                it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
            }
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(mediaId)
                    .setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
                    .setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
                    .setIconUri(Uri.parse(preferences.getString(RECENT_SONG_ICON_URI_KEY, "")))
                    .setExtras(extras)
                    .build(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
            )
        }
    }
}