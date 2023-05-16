package com.example.mvvmdemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.base.response.*
import com.example.mvvmdemo.base.viewmodel.AppViewModelFactory
import com.example.mvvmdemo.base.viewmodel.BaseAndroidViewModel
import com.example.mvvmdemo.base.viewmodel.BaseViewModel
import com.example.mvvmdemo.expand.findActualGenericsClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class BaseVMActivity<VM : BaseViewModel<*>, VB : ViewBinding> : AppCompatActivity() {
    val binding: ViewBinding by lazy {
        createViewBinding()
    }

    val viewModel: BaseViewModel<*> by lazy {
        createViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadlivedata.observe(
            this
        ) { t ->
            when (t.state) {
                DataState.STATE_LOADING -> {
                    showLoading(t.msg)
                }
                DataState.STATE_FINISH -> {
                    dismissLoading()
                }
                else -> {

                }
            }
        }

    }

    open fun createViewBinding(): VB {
        val binding = findActualGenericsClass<VB>(ViewBinding::class.java)
            ?: throw NullPointerException("Can not find a ViewBinding Generics in ${javaClass.simpleName}")
        try {
            val method = binding.getDeclaredMethod("inflate", layoutInflater::class.java)
            return method.invoke(null, layoutInflater) as VB
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw throw NullPointerException("create viewbinding failed ${javaClass.simpleName}")
    }

    open fun createViewModel(): VM {
        val actualGenericsClass = findActualGenericsClass<VM>(BaseViewModel::class.java)
            ?: throw NullPointerException("Can not find a ViewModel Generics in ${javaClass.simpleName}")

        if (Modifier.isAbstract(actualGenericsClass.modifiers)) {
            throw IllegalStateException("$actualGenericsClass is an abstract class,abstract ViewModel class can not create a instance!")
        }
        if (BaseAndroidViewModel::class.java.isAssignableFrom(actualGenericsClass)) {
            return ViewModelProvider(
                this,
                AppViewModelFactory(application)
            )[actualGenericsClass]
        }
        return ViewModelProvider(this)[actualGenericsClass]

    }

    /**
     * 显示Loading
     */
    open fun showLoading(msg: String? = null) {
        // ToastUtils.showToast("showLoading")
    }

    /**
     * 隐藏Loading
     */
    open fun dismissLoading() {
        // ToastUtils.showToast("hideLoading")
    }

    fun <T> parseResultAndCallback(
        response: ApiResponse<T>,
        listenerBuilder: ResultBuilder<T>.() -> Unit
    ) {
        val listener = ResultBuilder<T>().also(listenerBuilder)
        when (response) {
            is ApiSuccessResponse -> listener.onSuccess(response.response)
            is ApiEmptyResponse -> listener.onDataEmpty()
            is ApiFailedResponse -> listener.onFailed(response.errorCode, response.errorMsg)
            is ApiErrorResponse -> listener.onError(response.throwable)
        }
        listener.onComplete()
    }
}

/**
 * 返回flow对象
 *
 * @param T
 * @param block
 * @return
 */
fun <T> BaseVMActivity<*, *>.launchWithLoadingGetFlow(block: suspend () -> ApiResponse<T>): Flow<ApiResponse<T>> {
    return flow {
        emit(block())
    }.onStart {
        showLoading()
    }.onCompletion {
        dismissLoading()
    }
}

fun <T> BaseVMActivity<*, *>.launchWithLoadingGetLiveData(block: suspend () -> ApiResponse<T>): LiveData<ApiResponse<T>> {
    return launchWithLoadingGetFlow(block).asLiveData()
}

/**
 * 直接使用，回调方式
 *
 * @param T
 * @param block
 * @param listenerBuilder
 */
fun <T> BaseVMActivity<*, *>.launchWithLoadingAndCollect(
    block: suspend () -> ApiResponse<T>,
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {
    lifecycleScope.launch {
        launchWithLoadingGetFlow(block).collect { response ->
            parseResultAndCallback(response, listenerBuilder)
        }
    }
}

fun <T> BaseVMActivity<*, *>.launchAndCollect(
    requestBlock: suspend () -> ApiResponse<T>,
    startCallback: () -> Unit = { showLoading() },
    completeCallback: () -> Unit = { dismissLoading() },
    listenerBuilder: ResultBuilder<T>.() -> Unit
) {

}


