package com.example.mvvmdemo.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmdemo.base.livedata.ResponseLiveData
import com.example.mvvmdemo.base.livedata.ResponseMutableLiveData
import com.example.mvvmdemo.base.repository.BaseRepository
import com.example.mvvmdemo.base.response.ApiResponse
import com.example.mvvmdemo.expand.findActualGenericsClass
import com.example.mvvmdemo.net.parser.BaseResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class BaseViewModel<T : BaseRepository> : ViewModel() {

    val repository by lazy {
        createRepository()
    }

    val loadlivedata by lazy {
        repository.loadLivedata
    }

    private val castException = CoroutineExceptionHandler { coroutineContext, throwable ->

    }

    fun <T : Any> launch(
        response: MutableLiveData<ApiResponse<T>>,
        showloading: Boolean = true,
        showmsg: String = "", block: suspend () -> ApiResponse<T>
    ) {
        viewModelScope.launch(castException) {
            val result = repository.executeHttp { block() }
            response.value = result
        }
    }

    open fun createRepository(): T {
        val baseRepository = findActualGenericsClass<T>(BaseRepository::class.java)
            ?: throw throw NullPointerException("Can not find a BaseRepository Generics in ${javaClass.simpleName}")
        return baseRepository.newInstance()
    }

}