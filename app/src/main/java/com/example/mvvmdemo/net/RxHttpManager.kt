package com.example.mvvmdemo.net

import android.media.tv.TvInputManager
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
class RxHttpManager {
    companion object {
        val client by lazy {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }

    }

}