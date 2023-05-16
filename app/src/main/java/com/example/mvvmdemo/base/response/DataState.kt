package com.example.mvvmdemo.base.response

/**
 * @description 网络状态
 * @author Raymond
 * @date 2023/4/11
 *
 */
enum class DataState {
    STATE_LOADING, // 开始请求
    STATE_SUCCESS, // 服务器请求成功
    STATE_EMPTY, // 服务器返回数据为null
    STATE_FAILED, // 接口请求成功但是服务器返回error
    STATE_ERROR, // 请求失败
    STATE_FINISH, // 请求结束
}