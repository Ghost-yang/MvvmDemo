package com.example.mvvmdemo.net

import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
class RxHttpApi {
    suspend fun login() {
    val url = "www.baidu.com"
        RxHttp.postForm(url)
            .toAwait<String>()
            .await()
    }
}