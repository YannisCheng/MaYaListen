package com.yannis.baselib.net

import com.sensorsdata.analytics.android.sdk.cost.okhttp3.NetworkListener
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitManager2
 *
 * 参考：https://juejin.im/post/5ca9ac0fe51d452b616f2c7d
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/6/7 - 15:16
 */
class RetrofitManager2 private constructor() {

    companion object {
        @Volatile
        private var instance: RetrofitManager2? = null
        private lateinit var retrofit: Retrofit

        @JvmStatic
        fun getInstance(): RetrofitManager2 =
            instance ?: synchronized(this) { instance ?: RetrofitManager2() }
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .eventListenerFactory(NetworkListener.get())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            })
            // 多个baseurl动态切换
            .addInterceptor(BaseUrlInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(NetConstants.BASE_URL_XIMALYA)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    /**
     * 设置不同的Service，调用不同模块的接口服务
     */
    fun <T> getApi(service: Class<T>): T {
        return retrofit.create(service)
    }

}