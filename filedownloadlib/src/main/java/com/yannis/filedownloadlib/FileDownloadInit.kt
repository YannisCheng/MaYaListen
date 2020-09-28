package com.yannis.filedownloadlib

import android.app.Application
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection


/**
 * 文件下载器初始化
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/28
 */
open class FileDownloadInit(val application: Application) {

    /**
     * 解决下载任务突然卡死问题：https://www.jianshu.com/p/bf0db2cda0dd
     * 库readme：https://github.com/lingochamp/FileDownloader/blob/master/README-zh.md
     */
    init {
        FileDownloader.setupOnApplicationOnCreate(application)
            .connectionCreator(
                FileDownloadUrlConnection.Creator(
                    FileDownloadUrlConnection.Configuration()
                        // set connection timeout.
                        .connectTimeout(15000)
                        // set read timeout.
                        .readTimeout(15000)
                )
            )
            //下载文件块数是1,解决偶现的下载任务停止问题。
            .connectionCountAdapter { downloadId: Int, url: String?, path: String?, totalLength: Long ->
                1
            }
            .commit()
    }

}