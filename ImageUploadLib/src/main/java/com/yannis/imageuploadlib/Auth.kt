/*
package com.yannis.imageuploadlib

import com.google.gson.annotations.SerializedName
import com.qiniu.android.bigdata.client.Client
import com.qiniu.android.http.Headers
import com.qiniu.android.utils.StringMap
import com.qiniu.android.utils.UrlSafeBase64
import okio.internal.commonAsUtf8ToByteArray
import java.net.URI
import java.nio.charset.Charset
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList


*/
/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/28
 *//*

class Auth private constructor(accessKey: String, secretKeySpec: SecretKeySpec) {
    val accessKey: String
    private val secretKey: SecretKeySpec
    private fun createMac(): Mac {
        val mac: Mac
        try {
            mac = Mac.getInstance("HmacSHA1")
            mac.init(secretKey)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            throw IllegalArgumentException(e)
        }
        return mac
    }

    @Deprecated("") // private
    fun sign(data: ByteArray?): String {
        val mac: Mac = createMac()
        val encodedSign: String = UrlSafeBase64.encodeToString(mac.doFinal(data))
        return accessKey + ":" + encodedSign
    }

    @Deprecated("") // private
    fun sign(data: String?): String {
        return sign(StringUtils.utf8Bytes(data!!))
    }

    @Deprecated("") // private
    fun signWithData(data: ByteArray?): String {
        val s = UrlSafeBase64.encodeToString(data)
        return sign(StringUtils.utf8Bytes(s)) + ":" + s
    }

    @Deprecated("") // private
    fun signWithData(data: String?): String {
        return signWithData(StringUtils.utf8Bytes(data!!))
    }

    */
/**
 * 生成HTTP请求签名字符串
 *
 * @param urlString
 * @param body
 * @param contentType
 * @return
 *//*

    @Deprecated("") // private
    fun signRequest(urlString: String?, body: ByteArray?, contentType: String?): String {
        val uri: URI = URI.create(urlString)
        val path: String = uri.getRawPath()
        val query: String = uri.getRawQuery()
        val mac: Mac = createMac()
        mac.update(StringUtils.utf8Bytes(path))
        if (query != null && query.length != 0) {
            mac.update('?'.toByte())
            mac.update(StringUtils.utf8Bytes(query))
        }
        mac.update('\n'.toByte())
        if (body != null && Client.FormMime.equalsIgnoreCase(contentType)) {
            mac.update(body)
        }
        val digest: String = UrlSafeBase64.encodeToString(mac.doFinal())
        return accessKey + ":" + digest
    }

    */
/**
 * 验证回调签名是否正确
 *
 * @param originAuthorization 待验证签名字符串，以 "QBox "作为起始字符
 * @param url                 回调地址
 * @param body                回调请求体。原始请求体，不要解析后再封装成新的请求体--可能导致签名不一致。
 * @param contentType         回调ContentType
 * @return
 *//*

    fun isValidCallback(
        originAuthorization: String,
        url: String?,
        body: ByteArray?,
        contentType: String?
    ): Boolean {
        val authorization = "QBox " + signRequest(url, body, contentType)
        return authorization == originAuthorization
    }
    */
/**
 * 下载签名
 *
 * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
 * http://img.domain.com/u/3.jpg?imageView2/1/w/120
 * @param expires 有效时长，单位秒。默认3600s
 * @return
 *//*

    */
/**
 * 下载签名
 *
 * @param baseUrl 待签名文件url，如 http://img.domain.com/u/3.jpg 、
 * http://img.domain.com/u/3.jpg?imageView2/1/w/120
 * @return
 *//*

    @JvmOverloads
    fun privateDownloadUrl(baseUrl: String, expires: Long = 3600): String {
        val deadline = System.currentTimeMillis() / 1000 + expires
        return privateDownloadUrlWithDeadline(baseUrl, deadline)
    }

    fun privateDownloadUrlWithDeadline(baseUrl: String, deadline: Long): String {
        val b = StringBuilder()
        b.append(baseUrl)
        val pos = baseUrl.indexOf("?")
        if (pos > 0) {
            b.append("&e=")
        } else {
            b.append("?e=")
        }
        b.append(deadline)
        val token = sign(StringUtils.utf8Bytes(b.toString()))
        b.append("&token=")
        b.append(token)
        return b.toString()
    }
    */
/**
 * 生成上传token
 *
 * @param bucket  空间名
 * @param key     key，可为 null
 * @param expires 有效时长，单位秒。默认3600s
 * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
 * scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
 * @param strict  是否去除非限定的策略字段，默认true
 * @return 生成的上传token
 *//*

    */
/**
 * scope = bucket
 * 一般情况下可通过此方法获取token
 *
 * @param bucket 空间名
 * @return 生成的上传token
 *//*

    */
/**
 * scope = bucket:key
 * 同名文件覆盖操作、只能上传指定key的文件可以可通过此方法获取token
 *
 * @param bucket 空间名
 * @param key    key，可为 null
 * @return 生成的上传token
 *//*

    */
/**
 * 生成上传token
 *
 * @param bucket  空间名
 * @param key     key，可为 null
 * @param expires 有效时长，单位秒
 * @param policy  上传策略的其它参数，如 new StringMap().put("endUser", "uid").putNotEmpty("returnBody", "")。
 * scope通过 bucket、key间接设置，deadline 通过 expires 间接设置
 * @return 生成的上传token
 *//*

    @JvmOverloads
    fun uploadToken(
        bucket: String,
        key: String? = null,
        expires: Long = 3600,
        policy: StringMap? = null,
        strict: Boolean = true
    ): String {
        val deadline = System.currentTimeMillis() / 1000 + expires
        return uploadTokenWithDeadline(bucket, key, deadline, policy, strict)
    }

    fun uploadTokenWithDeadline(
        bucket: String,
        key: String?,
        deadline: Long,
        policy: StringMap?,
        strict: Boolean
    ): String {
        // TODO   UpHosts Global
        var scope = bucket
        if (key != null) {
            scope = "$bucket:$key"
        }
        val x = StringMap()
        copyPolicy(x, policy, strict)
        x.put("scope", scope)
        x.put("deadline", deadline)
        val s: String = Json.encode(x)
        return signWithData(StringUtils.utf8Bytes(s))
    }

    fun uploadTokenWithPolicy(obj: Any?): String {
        val s: String = Json.encode(obj)
        return signWithData(StringUtils.utf8Bytes(s))
    }

    @Deprecated("")
    fun authorization(url: String?, body: ByteArray?, contentType: String?): StringMap {
        val authorization = "QBox " + signRequest(url, body, contentType)
        return StringMap().put("Authorization", authorization)
    }

    @Deprecated("")
    fun authorization(url: String?): StringMap {
        return authorization(url, null, null)
    }

    */
/**
 * 生成HTTP请求签名字符串
 *
 * @param url
 * @param body
 * @param contentType
 * @return
 *//*

    @Deprecated("")
    fun signRequestV2(
        url: String?,
        method: String?,
        body: ByteArray?,
        contentType: String?
    ): String {
        return signQiniuAuthorization(url, method, body, contentType)
    }

    fun signQiniuAuthorization(
        url: String?,
        method: String?,
        body: ByteArray?,
        contentType: String?
    ): String {
        var headers: Headers? = null
        if (!StringUtils.isNullOrEmpty(contentType)) {
            headers = Headers.Builder().set("Content-Type", contentType).build()
        }
        return signQiniuAuthorization(url, method, body, headers)
    }

    fun signQiniuAuthorization(
        url: String?,
        method: String?,
        body: ByteArray?,
        headers: Headers?
    ): String {
        var method = method
        val uri: URI = URI.create(url)
        if (StringUtils.isNullOrEmpty(method)) {
            method = "GET"
        }
        val sb = StringBuilder()
        sb.append(method).append(" ").append(uri.getPath())
        if (uri.getQuery() != null) {
            sb.append("?").append(uri.getQuery())
        }
        sb.append("\nHost: ").append(if (uri.getHost() != null) uri.getHost() else "")
        if (uri.getPort() > 0) {
            sb.append(":").append(uri.getPort())
        }
        var contentType: String? = null
        if (null != headers) {
            contentType = headers.get("Content-Type")
            if (contentType != null) {
                sb.append("\nContent-Type: ").append(contentType)
            }
            val xQiniuheaders = genXQiniuHeader(headers)
            Collections.sort(xQiniuheaders)
            if (xQiniuheaders.size > 0) {
                for (h in xQiniuheaders) {
                    sb.append("\n").append(h.name).append(": ").append(h.value)
                }
            }
        }
        sb.append("\n\n")
        if (body != null && body.size > 0 && null != contentType && "" != contentType
            && "application/octet-stream" != contentType
        ) {
            sb.append(String(body))
        }
        val mac: Mac = createMac()
        mac.update(StringUtils.utf8Bytes(sb.toString()))
        val digest: String = UrlSafeBase64.encodeToString(mac.doFinal())
        return accessKey + ":" + digest
    }

    private fun genXQiniuHeader(headers: Headers): List<Header> {
        val hs = ArrayList<Header>()
        for (name in headers.names()) {
            if (name.length > "X-Qiniu-".length && name.startsWith("X-Qiniu-")) {
                for (value in headers.values(name)) {
                    hs.add(Header(canonicalMIMEHeaderKey(name), value))
                }
            }
        }
        return hs
    }

    fun qiniuAuthorization(
        url: String?,
        method: String?,
        body: ByteArray?,
        headers: Headers?
    ): Headers? {
        var headers: Headers? = headers
        val authorization = "Qiniu " + signQiniuAuthorization(url, method, body, headers)
        headers = if (headers == null) {
            Headers.Builder().set("Authorization", authorization).build()
        } else {
            headers.newBuilder().set("Authorization", authorization).build()
        }
        return headers
    }

    @Deprecated("")
    fun authorizationV2(
        url: String?,
        method: String?,
        body: ByteArray?,
        contentType: String?
    ): StringMap {
        val authorization = "Qiniu " + signRequestV2(url, method, body, contentType)
        return StringMap().put("Authorization", authorization)
    }

    @Deprecated("")
    fun authorizationV2(url: String?): StringMap {
        return authorizationV2(url, "GET", null, null)
    }

    //连麦 RoomToken
    @Throws(Exception::class)
    fun signRoomToken(roomAccess: String?): String {
        val encodedRoomAcc = UrlSafeBase64.encodeToString(roomAccess)
        val sign: ByteArray = createMac().doFinal(encodedRoomAcc.toByteArray())
        val encodedSign = UrlSafeBase64.encodeToString(sign)
        return accessKey + ":" + encodedSign + ":" + encodedRoomAcc
    }

    fun generateLinkingDeviceToken(
        appid: String?,
        deviceName: String?,
        deadline: Long,
        actions: Array<String?>
    ): String {
        val staments = arrayOfNulls<LinkingDtokenStatement>(actions.size)
        for (i in actions.indices) {
            staments[i] = LinkingDtokenStatement(actions[i])
        }
        val random = SecureRandom()
        val map = StringMap()
        map.put("appid", appid).put("device", deviceName).put("deadline", deadline)
            .put("random", random.nextInt()).put("statement", staments)
        val s: String = Json.encode(map)
        return signWithData(StringUtils.utf8Bytes(s))
    }

    fun generateLinkingDeviceTokenWithExpires(
        appid: String?, deviceName: String?,
        expires: Long, actions: Array<String?>
    ): String {
        val deadline = System.currentTimeMillis() / 1000 + expires
        return generateLinkingDeviceToken(appid, deviceName, deadline, actions)
    }

    fun generateLinkingDeviceVodTokenWithExpires(
        appid: String?,
        deviceName: String?,
        expires: Long
    ): String {
        return generateLinkingDeviceTokenWithExpires(
            appid, deviceName, expires, arrayOf(
                DTOKEN_ACTION_VOD
            )
        )
    }

    fun generateLinkingDeviceStatusTokenWithExpires(
        appid: String?,
        deviceName: String?,
        expires: Long
    ): String {
        return generateLinkingDeviceTokenWithExpires(
            appid, deviceName, expires, arrayOf(
                DTOKEN_ACTION_STATUS
            )
        )
    }

    internal inner class LinkingDtokenStatement(@field:SerializedName("action") var action: String?)
    private inner class Header internal constructor(var name: String, var value: String) :
        Comparable<Header> {
        override operator fun compareTo(other: Header): Int {
            val c = name.compareTo(other.name)
            return if (c == 0) {
                value.compareTo(other.value)
            } else {
                c
            }
        }
    }

    companion object {
        const val DTOKEN_ACTION_VOD = "linking:vod"
        const val DTOKEN_ACTION_STATUS = "linking:status"
        const val DTOKEN_ACTION_TUTK = "linking:tutk"

        */
/**
 * 上传策略
 * 参考文档：[上传策略](https://developer.qiniu.com/kodo/manual/put-policy)
 *//*

        private val policyFields = arrayOf(
            "callbackUrl",
            "callbackBody",
            "callbackHost",
            "callbackBodyType",
            "callbackFetchKey",
            "returnUrl",
            "returnBody",
            "endUser",
            "saveKey",
            "insertOnly",
            "isPrefixalScope",
            "detectMime",
            "mimeLimit",
            "fsizeLimit",
            "fsizeMin",
            "persistentOps",
            "persistentNotifyUrl",
            "persistentPipeline",
            "deleteAfterDays",
            "fileType"
        )
        private val deprecatedPolicyFields = arrayOf(
            "asyncOps"
        )
        private val isTokenTable = genTokenTable()
        private const val toLower = 'a' - 'A'
        fun create(accessKey: String, secretKey: String?): Auth {
            require(!(StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey))) { "empty key" }
            val sk = StringUtils.utf8Bytes(secretKey!!)
            val secretKeySpec = SecretKeySpec(sk, "HmacSHA1")
            return Auth(accessKey, secretKeySpec)
        }

        private fun copyPolicy(policy: StringMap, originPolicy: StringMap?, strict: Boolean) {
            if (originPolicy == null) {
                return
            }
            originPolicy.forEach(StringMap.Consumer { key, value ->
                require(
                    !StringUtils.inStringArray(
                        key,
                        deprecatedPolicyFields
                    )
                ) { "$key is deprecated!" }
                if (!strict || StringUtils.inStringArray(key, policyFields)) {
                    policy.put(key, value)
                }
            })
        }

        // https://github.com/golang/go/blob/master/src/net/textproto/reader.go#L596
        // CanonicalMIMEHeaderKey returns the canonical format of the
        // MIME header key s. The canonicalization converts the first
        // letter and any letter following a hyphen to upper case;
        // the rest are converted to lowercase. For example, the
        // canonical key for "accept-encoding" is "Accept-Encoding".
        // MIME header keys are assumed to be ASCII only.
        // If s contains a space or invalid header field bytes, it is
        // returned without modifications.
        private fun canonicalMIMEHeaderKey(name: String): String {
            // com.qiniu.http.Headers 已确保 header name 字符的合法性，直接使用 byte ，否则要使用 char //
            val a: ByteArray = name.commonAsUtf8ToByteArray()
            for (i in a.indices) {
                val c = a[i]
                if (!validHeaderFieldByte(c)) {
                    return name
                }
            }
            var upper = true
            for (i in a.indices) {
                var c = a[i]
                if (upper && 'a'.toByte() <= c && c <= 'z'.toByte()) {
                    (c -= toLower.toByte()).toByte()
                } else if (!upper && 'A'.toByte() <= c && c <= 'Z'.toByte()) {
                    (c += toLower.toByte()).toByte()
                }
                a[i] = c
                upper = c == '-'.toByte() // for next time
            }
            return String(a)
        }

        private fun validHeaderFieldByte(b: Byte): Boolean {
            //byte: -128 ~ 127, char:  0 ~ 65535
            return 0 < b && b < isTokenTable.size && isTokenTable[b.toInt()]
        }

        private fun genTokenTable(): BooleanArray {
            val idx = intArrayOf(
                '!'.toInt(),
                '#'.toInt(),
                '$'.toInt(),
                '%'.toInt(),
                '&'.toInt(),
                '\''.toInt(),
                '*'.toInt(),
                '+'.toInt(),
                '-'.toInt(),
                '.'.toInt(),
                '0'.toInt(),
                '1'.toInt(),
                '2'.toInt(),
                '3'.toInt(),
                '4'.toInt(),
                '5'.toInt(),
                '6'.toInt(),
                '7'.toInt(),
                '8'.toInt(),
                '9'.toInt(),
                'A'.toInt(),
                'B'.toInt(),
                'C'.toInt(),
                'D'.toInt(),
                'E'.toInt(),
                'F'.toInt(),
                'G'.toInt(),
                'H'.toInt(),
                'I'.toInt(),
                'J'.toInt(),
                'K'.toInt(),
                'L'.toInt(),
                'M'.toInt(),
                'N'.toInt(),
                'O'.toInt(),
                'P'.toInt(),
                'Q'.toInt(),
                'R'.toInt(),
                'S'.toInt(),
                'T'.toInt(),
                'U'.toInt(),
                'W'.toInt(),
                'V'.toInt(),
                'X'.toInt(),
                'Y'.toInt(),
                'Z'.toInt(),
                '^'.toInt(),
                '_'.toInt(),
                '`'.toInt(),
                'a'.toInt(),
                'b'.toInt(),
                'c'.toInt(),
                'd'.toInt(),
                'e'.toInt(),
                'f'.toInt(),
                'g'.toInt(),
                'h'.toInt(),
                'i'.toInt(),
                'j'.toInt(),
                'k'.toInt(),
                'l'.toInt(),
                'm'.toInt(),
                'n'.toInt(),
                'o'.toInt(),
                'p'.toInt(),
                'q'.toInt(),
                'r'.toInt(),
                's'.toInt(),
                't'.toInt(),
                'u'.toInt(),
                'v'.toInt(),
                'w'.toInt(),
                'x'.toInt(),
                'y'.toInt(),
                'z'.toInt(),
                '|'.toInt(),
                '~'.toInt()
            )
            val tokenTable = BooleanArray(127)
            Arrays.fill(tokenTable, false)
            for (i in idx) {
                tokenTable[i] = true
            }
            return tokenTable
        }
    }

    init {
        this.accessKey = accessKey
        secretKey = secretKeySpec
    }
}*/
