package com.cwj.exoplayerlib.extensions

import android.content.ContentResolver
import android.net.Uri
import java.io.File

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/11/26
 */
/**
 * 含java.io.File类的扩展方法,返回AlbumArtContentProvider的Content Uri
 */
private const val AUTHORITY = "com.cwj.exoplayerlib.library.AlbumArtContentProvider"
fun File.asAlbumArtContentUri(): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(AUTHORITY)
        .appendPath(this.path)
        .build()
}