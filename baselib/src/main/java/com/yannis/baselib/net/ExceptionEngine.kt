package com.yannis.baselib.net

import com.google.gson.JsonParseException
import com.yannis.baselib.net.NetErrorConstant.CONNECT
import com.yannis.baselib.net.NetErrorConstant.EOFE
import com.yannis.baselib.net.NetErrorConstant.INTERRUPTED
import com.yannis.baselib.net.NetErrorConstant.PARSE
import com.yannis.baselib.net.NetErrorConstant.SOCKET_TIME_OUT
import com.yannis.baselib.net.NetErrorConstant.SSL
import com.yannis.baselib.net.NetErrorConstant.UN_KNOWN_HOST_NAME
import org.json.JSONException
import retrofit2.HttpException
import java.io.EOFException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * ExceptionEngine 异常统一处理
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/4
 */
object ExceptionEngine {

    fun handleException(e: Throwable): RequestThrowable {
        var requestThrowable: RequestThrowable = RequestThrowable(0, "")
        if (e is EOFException) {
            // 连接丢失
            requestThrowable = RequestThrowable(EOFE, "连接丢失")
        } else if (e is SocketTimeoutException) {
            // 请求超时
            requestThrowable = RequestThrowable(SOCKET_TIME_OUT, "请求超时")
        } else if (e is SSLHandshakeException) {
            // 安全证书异常
            requestThrowable = RequestThrowable(SSL, "安全证书异常")
        } else if (e is ConnectException) {
            // 网络连接异常
            requestThrowable = RequestThrowable(CONNECT, "网络连接异常")
        } else if (e is UnknownHostException) {
            // 域名解析失败
            requestThrowable = RequestThrowable(UN_KNOWN_HOST_NAME, "域名解析失败")
        } else if (e is JsonParseException || e is JSONException/* || e is ParseException*/) {
            // 数据解析异常
            requestThrowable = RequestThrowable(PARSE, "数据解析异常")
        } else if (e is InterruptedIOException) {
            // 连接中断
            requestThrowable = RequestThrowable(INTERRUPTED, "连接中断")
        } else if (e is HttpException) {
            val code = (e as HttpException).code()
            when (code) {
                // 参考：https://blog.csdn.net/csdn1844295154/article/details/78980174?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase
                500 -> RequestThrowable(500, "服务器内部错误")
                501 -> RequestThrowable(501, "服务器不具备完成请求的功能")
                502 -> RequestThrowable(502,"错误网关")
                503 -> RequestThrowable(503,"服务不可用")
                504 -> RequestThrowable(504,"网关超时")
                //505 -> "HTTP版本不支持"
                400 -> RequestThrowable(400,"错误请求")
                401 -> RequestThrowable(401,"未经授权，请求需要身份验证")
                403 -> RequestThrowable(400,"服务器拒绝请求")
                404 -> RequestThrowable(404,"服务器找不到请求的网页")
                //405 -> "方法不允许"
                //406 -> "不可接受"
                //407 -> "需要代理身份验证"
                408 -> RequestThrowable(408,"请求超时")
                //410 -> "文档永久地离开了指定的位置"
                //411 -> "需要Content-Length头请求"
                //413 -> "请求实体太大"
                //414 -> "请求URI太长"
                //415 -> "不支持的媒体类型"
            }
        } else {
            // 未知异常
        }
        return requestThrowable
    }

}