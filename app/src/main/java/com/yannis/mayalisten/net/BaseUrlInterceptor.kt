package com.yannis.mayalisten.net

import com.yannis.mayalisten.constant.NetConstants.Companion.BASE_URL_BILIBILI
import com.yannis.mayalisten.constant.NetConstants.Companion.BASE_URL_STR_BILIBILI
import com.yannis.mayalisten.constant.NetConstants.Companion.BASE_URL_STR_XIMALAY
import com.yannis.mayalisten.constant.NetConstants.Companion.BASE_URL_XIMALYA
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
class BaseUrlInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request:Request = chain.request();
        val newBuilder = request.newBuilder()
        val url:HttpUrl = request.url
        val urls:List<String> = request.headers("base_url")
        if (urls.isNotEmpty()) {
            newBuilder.removeHeader("base_url")
            val headerBaseUrl:String = urls[0]
            val newHttpUrl:HttpUrl
            newHttpUrl = if (BASE_URL_STR_XIMALAY == headerBaseUrl) {
                BASE_URL_XIMALYA.toHttpUrlOrNull()!!
            } else if (BASE_URL_STR_BILIBILI == headerBaseUrl) {
                BASE_URL_BILIBILI.toHttpUrlOrNull()!!
            } else {
                url
            }

            val build = url.newBuilder()
                //.scheme()
                .host(newHttpUrl.host)
                .port(newHttpUrl.port)
                .build()
            return chain.proceed(newBuilder.url(build).build())
        }
        return chain.proceed(request)
    }
}