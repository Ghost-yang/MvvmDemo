package com.example.mvvmdemo.base

import android.app.Application
import com.example.mvvmdemo.net.RxHttpManager
import rxhttp.RxHttpPlugins

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        RxHttpPlugins.init(RxHttpManager.client)
            .setDebug(true)
            .setOnParamAssembly {

            }
    }
}