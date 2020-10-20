package com.yannis.mayalisten.old

/**
 * RetrofitManager2
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/6/7 - 15:16
 */
/*
class RetrofitManager2 private constructor() {

    companion object {
        private lateinit var api: MaYaApi

        @Volatile
        private var instance: RetrofitManager2? = null

        @JvmStatic
        fun getInstance(): RetrofitManager2 =
            instance ?: synchronized(this) { instance ?: RetrofitManager2() }
    }

    init {
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
            .baseUrl(NetConstants.BASE_URL_XIMALYA)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MaYaApi::class.java)
    }


    fun getApi(): MaYaApi {
        return api
    }

}*/
