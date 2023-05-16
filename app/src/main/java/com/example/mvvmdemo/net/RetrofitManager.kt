package com.example.mvvmdemo.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
class RetrofitManager {
    val client by lazy {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }

    val retrofitClient by lazy {
        Retrofit.Builder().baseUrl("https://www.wanandroid.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api by lazy {
        retrofitClient.create(Api::class.java)
    }

    inline fun <reified T : Class<*>> createSeviceApi(value: T): T {
        return retrofitClient.create(T::class.java)
    }

    companion object {
        val instance by lazy {
            RetrofitManager()
        }
    }
}

