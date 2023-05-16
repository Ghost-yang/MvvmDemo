package com.example.mvvmdemo.base.repository

import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemo.BuildConfig
import com.example.mvvmdemo.base.livedata.ResponseMutableLiveData
import com.example.mvvmdemo.base.response.*
import kotlinx.coroutines.delay

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class BaseRepository {
    val loadLivedata by lazy {
        MutableLiveData<LoadingState>()
    }

    suspend fun <T : Any> excute(
        block: suspend () -> T,
        responseLiveData: ResponseMutableLiveData<T>,
        showloading: Boolean,
        showmsg: String
    ) {
        if (showloading) {
            loadLivedata.postValue(LoadingState(showmsg, DataState.STATE_LOADING))
        }
        val response = BaseResponse<T>()
        try {
            val data = block.invoke()
            response.data = data
            response.dataState = DataState.STATE_SUCCESS
        } catch (e: Exception) {
            response.exception = e
            response.dataState = DataState.STATE_ERROR
        } finally {
            responseLiveData.postValue(response)
            if (showloading) {
                loadLivedata.postValue(LoadingState(showmsg, DataState.STATE_FINISH))
            }
        }

    }

    suspend fun <T : Any> execteWithResult(
        block: suspend () -> BaseResponse<T>,
        showloading: Boolean,
        showmsg: String
    ): BaseResponse<T> {
        if (showloading) {
            loadLivedata.postValue(LoadingState(showmsg, DataState.STATE_LOADING))
        }
        runCatching { block() }
            .onSuccess {
                it.dataState = DataState.STATE_SUCCESS
                return it
            }.onFailure {
                val response = BaseResponse<T>(exception = it)
                return response
            }
        return BaseResponse<T>()
    }


    suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        //for test
        runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ApiEmptyResponse()
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
//        handlingExceptions(e)
        return ApiErrorResponse(e)
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
        return if (data.isSuccess) {
            getHttpSuccessResponse(data)
        } else {
//            handlingApiExceptions(data.errorCode, data.errorMsg)
            ApiFailedResponse(data.errorCode, data.errorMsg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            ApiEmptyResponse()
        } else {
            ApiSuccessResponse(data)
        }
    }

}