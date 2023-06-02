package com.example.mvvmdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.response.ApiResponse
import com.example.mvvmdemo.base.viewmodel.BaseViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.concurrent.thread

/**
 * @description
 * @author Raymond
 * @date 2023/4/21
 *
 */
class MainViewModel<T> : BaseViewModel<MainRepository>() {
    private val _livedata = MutableLiveData<ApiResponse<T>>()
    val liveData: LiveData<ApiResponse<T>> = _livedata
    private fun getInfo() = flow<String> {
        repository.getInfo()
        emit("info")
    }.onStart { }
        .onCompletion { }

    fun getName() {
        viewModelScope.launch {
            getInfo().collect {

            }
        }
    }

    fun testMap() = {
        flow<Int> {
            emit(1)
        }.map {

        }
    }

    val resultFlow = callbackFlow<String> {
        val result = "结果"
        trySend(result).isSuccess
        awaitClose {

        }
    }

    val job = viewModelScope.launch {
        resultFlow.collect {

        }
    }


}