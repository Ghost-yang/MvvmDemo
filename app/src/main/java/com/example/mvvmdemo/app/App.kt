package com.example.mvvmdemo.app

import android.app.Application
import com.example.mvvmdemo.net.RxHttpManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import rxhttp.RxHttpPlugins


/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        RxHttpPlugins.init(RxHttpManager.client)
            .setDebug(true)
            .setOnParamAssembly {

            }

        initLogger()
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(10)
            .tag(TAG).build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }

    companion object {
        const val TAG = "GTV"
    }
}