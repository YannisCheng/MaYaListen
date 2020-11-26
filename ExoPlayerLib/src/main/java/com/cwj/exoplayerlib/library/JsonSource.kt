package com.yannis.mayalisten.audio

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.cwj.exoplayerlib.extensions.*
import com.example.android.uamp.media.extensions.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * 从基本JSON流创建的MediaMetadataCompat对象的来源。
 * JSON的定义在此文件的JsonMusic文档中指定，这是它的对象表示
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
private const val TAG = "JsonSource"

class JsonSource(private val sourceUri: Uri) : AbstractMusicSource() {

    init {
        state = STATE_INITIALIZING
    }

    private var catalog = emptyList<MediaMetadataCompat>()

    override suspend fun loadSource() {
        updateCatalog(sourceUri)?.let { updatedCatalog ->
            {
                catalog = updatedCatalog
                state = STATE_INITIALIZED
            } ?: run {
                catalog = emptyList();
                state = STATE_ERROR
            }
        }
    }

    /**
     * kotlin协程挂起函数
     */
    private suspend fun updateCatalog(sourceUri: Uri): List<MediaMetadataCompat>? {
        return withContext(Dispatchers.IO) {
            val musicCat = try {
                downloadJson(sourceUri)
            } catch (ioException: IOException) {
                return@withContext null
            }

            // 获取基本URI，以在以后修正相对引用。
            val baseUri = sourceUri.toString().removeSuffix(sourceUri.lastPathSegment ?: "")
            val mediaMetadataCompats = musicCat.music.map { song ->
                // JSON可能具有相对于JSON本身来源的路径，以将它们转变为绝对路径。
                sourceUri.scheme?.let { scheme ->
                    if (!song.source.startsWith(scheme)) {
                        song.source = baseUri + song.source
                    }
                    if (!song.image.startsWith(scheme)) {
                        song.image = baseUri + song.image
                    }
                }

                MediaMetadataCompat.Builder()
                    .from(song)
                    .apply {
                        // Used by ExoPlayer and Notification
                        displayIconUri = song.image
                        albumArtUri = song.image
                    }
                    .build()

            }.toList() as List<MediaMetadataCompat>

            // 添加在宣布元数据更改时供ExoPlayer MediaSession扩展使用的描述键。
            mediaMetadataCompats.forEach { it.description.extras?.putAll(it.bundle) }
            Log.e(TAG, "updateCatalog: $mediaMetadataCompats")
            mediaMetadataCompats
        }
    }


    private fun downloadJson(sourceUri: Uri): JsonCatalog {
        val catalogConn = URL(sourceUri.toString())
        val reader = BufferedReader(InputStreamReader(catalogConn.openStream()))
        return Gson().fromJson(reader, JsonCatalog::class.java)
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.listIterator()
}

/**
 * MediaMetadataCompat.Builder的扩展方法，用于设置JSON构造对象中的字段（使代码更易于查看）
 */
fun MediaMetadataCompat.Builder.from(jsonMusic: JsonMusic): MediaMetadataCompat.Builder {
    val durationMs = TimeUnit.SECONDS.toMillis(jsonMusic.duration)

    id = jsonMusic.id
    title = jsonMusic.title
    artist = jsonMusic.artist
    album = jsonMusic.album
    duration = durationMs
    genre = jsonMusic.genre
    mediaUri = jsonMusic.source
    albumArtUri = jsonMusic.image
    trackNumber = jsonMusic.trackNumber
    trackCount = jsonMusic.totalTrackCount
    flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

    // 为了使显示这些内容更加容易，还请设置显示属性。
    displayTitle = jsonMusic.title
    displaySubtitle = jsonMusic.artist
    displayDescription = jsonMusic.album
    displayIconUri = jsonMusic.image

    // 添加downloadStatus以强制在生成的MediaMetadataCompat对象中创建“extras”捆绑软件。
    // 在更新过程中需要将准确的元数据发送到媒体会话。
    downloadStatus = MediaDescriptionCompat.STATUS_NOT_DOWNLOADED
    return this
}

class JsonCatalog {
    var music: ArrayList<JsonMusic> = ArrayList()
}

class JsonMusic {
    var id: String = ""
    var title: String = ""
    var album: String = ""
    var artist: String = ""
    var genre: String = ""
    var source: String = ""
    var image: String = ""
    var trackNumber: Long = 0
    var totalTrackCount: Long = 0
    var duration: Long = -1
    var site: String = ""
}