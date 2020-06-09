package com.yannis.mayalisten.net

import com.yannis.mayalisten.constant.NetConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/7
 */
class RetrofitManager {

    private var api: MaYaApi

    /**
     * 单例模式
     */
    companion object {
        @Volatile
        private var instance: RetrofitManager? = null

        fun getInstance(): RetrofitManager {
            return instance ?: synchronized(this) {
                instance ?: RetrofitManager()
            }
        }
    }

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(Interceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader("Content-Type", "application/json")
                        .build()
                )
            })
            .build()

        api = Retrofit.Builder()
            .baseUrl(NetConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MaYaApi::class.java)
    }


    fun getApi(): MaYaApi {
        return api
    }

}