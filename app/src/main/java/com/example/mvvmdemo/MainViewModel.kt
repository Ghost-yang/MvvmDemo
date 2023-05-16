package com.example.mvvmdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.response.ApiResponse
import com.example.mvvmdemo.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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


}