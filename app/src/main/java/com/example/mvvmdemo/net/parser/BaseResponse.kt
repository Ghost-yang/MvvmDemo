package com.example.mvvmdemo.net.parser

/**
 * @description
 * @author Raymond
 * @date 2023/4/13
 *
 */
data class BaseResponse<T>(var data: T, var code: Int, var msg: String)
