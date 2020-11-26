package com.yannis.mayalisten.audio


import android.os.Build
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.IntDef

/**
 * MusicService用于查找MediaMetadataCompat对象的接口。
 * 由于Kotlin提供了Iterable.find和Iterable.filter之类的方法，因此这是在源代码上使用的便捷接口。
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
interface MusicSource : Iterable<MediaMetadataCompat> {

    /**
     * Kotlin协程中的挂起函数
     * 开始加载此音乐源的数据
     */
    suspend fun loadSource()

    /**
     * 准备使用此MusicSource后，将执行给定操作的方法。
     * performAction当源准备好时，将使用布尔参数调用lambda表达式。true表示已成功准备源， false表示发生错误
     */
    fun whenReady(performAction: (Boolean) -> Unit): Boolean

    // fun search()
}

// - - - - - Sate Source各个阶段状态 - - - - -
/**
 * 指示source已创建但未执行初始化的状态。
 */
const val STATE_CREATED = 1

/**
 * 指示source初始化的状态正在进行中。
 */
const val STATE_INITIALIZING = 2

/**
 * 指示source已初始化并准备使用的状态。
 */
const val STATE_INITIALIZED = 3

/**
 * 指示发生错误的状态。
 */
const val STATE_ERROR = 4

@IntDef(
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

// - - - - - AbstractMusicSource - - - - -

/**
 * 音乐source的基类
 */
abstract class AbstractMusicSource : MusicSource {

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    var state = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }

        }

    /**
     * 当此MusicSource准备就绪时，执行操作。
     * 此方法不是线程安全的。 确保仅在单个线程上执行操作和状态更改
     */
    override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
        when (state) {
            STATE_CREATED, STATE_INITIALIZING -> {
                onReadyListeners += performAction
                false
            }
            else -> {
                performAction(state != STATE_ERROR)
                true
            }
        }


    /**
     * API 19缺少MediaStore.EXTRA_MEDIA_GENRE 。使用我们自己的版本隐藏此事实
     */
    private val EXTRA_MEDIA_GENRE
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MediaStore.EXTRA_MEDIA_GENRE
            } else {
                "android.intent.extra.genre"
            }
}