package com.yannis.baselib.net

import android.util.Log
import com.yannis.baselib.net.NetConstants.Companion.BASE_URL_STR_BILIBILI
import com.yannis.baselib.net.NetConstants.Companion.BASE_URL_XIMALYA
import com.yannis.baselib.net.NetConstants.Companion.BILIBILI
import com.yannis.baselib.net.NetConstants.Companion.XIMALAY
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * BaseUrlInterceptor BaseUrl拦截器
 *
 * okhttpclient拦截器，先捕获已经添加在Headers种的Url参数，然后修改baseURL
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/23
 */
class BaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()
        // 以原Request的配置为基础builder出一个新request
        val newBuilder = request.newBuilder()
        // 从request中获取原有的HttpUrl实例oldHttpUrl
        val oldUrl: HttpUrl = request.url
        val urls: List<String> = request.headers("baseurl")

        if (urls.isNotEmpty()) {
            // 从新request中移除原Headers中的baseurl配置，为设置新url做准备
            newBuilder.removeHeader("baseurl")
            val headerBaseUrl: String = urls[0]
            val newHttpUrl: HttpUrl
            newHttpUrl = if (XIMALAY == headerBaseUrl) {
                BASE_URL_XIMALYA.toHttpUrlOrNull()!!
            } else if (BASE_URL_STR_BILIBILI == headerBaseUrl) {
                BILIBILI.toHttpUrlOrNull()!!
            } else {
                oldUrl
            }

            // 以原HttpUrl为基础更新配置配置
            val build = oldUrl.newBuilder()
                //.scheme()
                .host(newHttpUrl.host)
                .port(newHttpUrl.port)
                .build()
            Log.e("TAG", "intercept: " + newHttpUrl.toString())
            return chain.proceed(newBuilder.url(build).build())
        }
        return chain.proceed(request)
    }
}