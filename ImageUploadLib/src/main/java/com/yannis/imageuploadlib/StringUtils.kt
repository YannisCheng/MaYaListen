package com.yannis.imageuploadlib

import com.qiniu.android.common.Constants
import com.qiniu.android.dns.util.Hex
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/28
 */
object StringUtils {
    /**
     * @see .join
     */
    /**
     * @see .join
     */
    @JvmOverloads
    fun join(list: Collection<*>?, sep: String?, prefix: String? = null): String {
        val array = list?.toTypedArray()
        return join(array, sep, prefix)
    }
    /**
     * 以指定的分隔符来进行字符串元素连接
     *
     *
     * 例如有字符串数组array和连接符为逗号(,)
     * `
     * String[] array = new String[] { "hello", "world", "qiniu", "cloud","storage" };
    ` *
     * 那么得到的结果是:
     * `
     * hello,world,qiniu,cloud,storage
    ` *
     *
     *
     * @param array  需要连接的对象数组
     * @param sep    元素连接之间的分隔符
     * @param prefix 前缀字符串
     * @return 连接好的新字符串
     */
    /**
     * @see .join
     */
    @JvmOverloads
    fun join(array: Array<Any?>?, sep: String?, prefix: String? = null): String {
        var sep = sep
        var prefix = prefix
        if (array == null) {
            return ""
        }
        val arraySize = array.size
        if (arraySize == 0) {
            return ""
        }
        if (sep == null) {
            sep = ""
        }
        if (prefix == null) {
            prefix = ""
        }
        val buf = StringBuilder(prefix)
        for (i in 0 until arraySize) {
            if (i > 0) {
                buf.append(sep)
            }
            buf.append(if (array[i] == null) "" else array[i])
        }
        return buf.toString()
    }

    /**
     * 以json元素的方式连接字符串中元素
     *
     *
     * 例如有字符串数组array
     * `
     * String[] array = new String[] { "hello", "world", "qiniu", "cloud","storage" };
    ` *
     * 那么得到的结果是:
     * `
     * "hello","world","qiniu","cloud","storage"
    ` *
     *
     *
     * @param array 需要连接的字符串数组
     * @return 以json元素方式连接好的新字符串
     */
    fun jsonJoin(array: Array<String>): String {
        val arraySize = array.size
        val bufSize = arraySize * (array[0].length + 3)
        val buf = StringBuilder(bufSize)
        for (i in 0 until arraySize) {
            if (i > 0) {
                buf.append(',')
            }
            buf.append('"')
            buf.append(array[i])
            buf.append('"')
        }
        return buf.toString()
    }

    fun isNullOrEmpty(s: String?): Boolean {
        return s == null || "" == s
    }

    fun inStringArray(s: String, array: Array<String>): Boolean {
        for (x in array) {
            if (x == s) {
                return true
            }
        }
        return false
    }

    fun utf8Bytes(data: String): ByteArray {
        return data.toByteArray(charset(Constants.UTF_8))
    }

    fun utf8String(data: ByteArray?): String {
        return String(data!!, Constants.UTF_8 as Charset)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun md5Lower(src: String): String {
        val digest = MessageDigest.getInstance("MD5")
        digest.update(src.toByteArray(Charset.forName("UTF-8")))
        val md5Bytes = digest.digest()
        return Hex.encodeHexString(md5Bytes)
    }
}