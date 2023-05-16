package com.example.mvvmdemo.base.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.mvvmdemo.base.response.*

/**
 * @description
 * @author Raymond
 * @date 2023/4/11
 *
 */
class ResponseMutableLiveData<T> : ResponseLiveData<T> {

    /**
     * Creates a MutableLiveData initialized with the given `value`.
     *
     *  @param  value initial value
     */
    constructor(value: BaseResponse<T>?) : super(value)

    /**
     * Creates a MutableLiveData with no value assigned to it.
     */
    constructor() : super()

    public override fun postValue(value: BaseResponse<T>?) {
        super.postValue(value)
    }

    public override fun setValue(value: BaseResponse<T>?) {
        super.setValue(value)
    }
}

/**
 * 利用扩展函数实现
 *
 * @param T
 * @param lifecycleOwner
 * @param listenerbuilder
 */
fun <T> LiveData<ApiResponse<T>>.observeState(
    lifecycleOwner: LifecycleOwner,
    listenerbuilder: ResultBuilder<T>.() -> Unit
) {
    val listener = ResultBuilder<T>().apply(listenerbuilder)
    observe(lifecycleOwner) {
        when (it) {
            is ApiSuccessResponse -> listener.onSuccess(it.data)
            is ApiEmptyResponse -> listener.onDataEmpty()
            is ApiErrorResponse -> listener.onError(it.throwable)
            is ApiFailedResponse -> listener.onFailed(it.errorCode, it.errorMsg)
        }
        listener.onComplete()
    }
}
