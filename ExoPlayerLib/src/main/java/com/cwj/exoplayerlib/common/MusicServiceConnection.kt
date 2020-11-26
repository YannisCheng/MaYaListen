package com.cwj.exoplayerlib.common

import android.content.ComponentName
import android.content.Context

/**
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
}