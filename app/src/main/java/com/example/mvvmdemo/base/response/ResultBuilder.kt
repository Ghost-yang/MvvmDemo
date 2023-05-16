package com.example.mvvmdemo.base.response

import com.bumptech.glide.util.Util

/**
 * @description
 * @author Raymond
 * @date 2023/4/23
 *
 */
class ResultBuilder<T> {
    var onSuccess: (data: T?) -> Unit = {}
    var onDataEmpty: () -> Unit = {}
    var onFailed: (errorcode: Int?, errorMsg: String?) -> Unit = { _, errorMsg ->

    }
    var onError: (e: Throwable) -> Unit = { e ->

    }
    var onComplete: () -> Unit = {

    }
}

fun <T> ApiResponse<T>.parseData(listenerBuilder: ResultBuilder<T>.() -> Unit) {
    val listener = ResultBuilder<T>().also(listenerBuilder)
    when (this) {
        is ApiSuccessResponse -> listener.onSuccess(this.response)
        is ApiEmptyResponse -> listener.onDataEmpty()
        is ApiFailedResponse -> listener.onFailed(this.errorCode, this.errorMsg)
        is ApiErrorResponse -> listener.onError(this.throwable)
    }
    listener.onComplete()
}