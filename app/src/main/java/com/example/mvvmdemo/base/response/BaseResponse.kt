package com.example.mvvmdemo.base.response

/**
 * @description 返回数据实体
 * @author Raymond
 * @date 2023/4/11
 *
 */
class BaseResponse<T>(
    var errorCode: Int = -1,
    var errorMsg: String? = null,
    var data: T? = null,
    var dataState: DataState? = null,
    var exception: Throwable? = null,

    ) {
    companion object {
        const val ERROR_CODE_SUCCESS = 0
    }

    val success: Boolean
        get() = errorCode == ERROR_CODE_SUCCESS

}