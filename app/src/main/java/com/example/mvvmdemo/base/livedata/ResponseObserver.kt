package com.example.mvvmdemo.base.livedata

import androidx.lifecycle.Observer
import com.example.mvvmdemo.base.response.BaseResponse
import com.example.mvvmdemo.base.response.DataState

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class ResponseObserver<T> : Observer<BaseResponse<T>> {

    override fun onChanged(t: BaseResponse<T>?) {
        t?.run {
            when (t.dataState) {
                DataState.STATE_SUCCESS -> {
                    onSuccess(t.data)
                }
                DataState.STATE_ERROR -> {
                    t.exception?.let { onException(it) }
                }
                else -> {

                }
            }
        }
    }

    abstract fun onSuccess(data: T?)

    private fun onException(e: Throwable) {

    }

    open fun onFailure() {

    }

}