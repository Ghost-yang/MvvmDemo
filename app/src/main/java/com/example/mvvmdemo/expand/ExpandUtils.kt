package com.example.mvvmdemo.expand

import java.lang.reflect.ParameterizedType

/**
 * @description
 * @author Raymond
 * @date 2023/4/11
 *
 */

internal fun <T> Any.findActualGenericsClass(cls: Class<*>): Class<T>? {
    val genericSuperclass = javaClass.genericSuperclass
    if (genericSuperclass !is ParameterizedType) {
        return null
    }
// 获取类的所有泛型参数数组
    val actualTypeArguments = genericSuperclass.actualTypeArguments
// 遍历泛型数组
    actualTypeArguments.forEach {
        if (it is Class<*> && cls.isAssignableFrom(it)) {
            return it as Class<T>
        } else if (it is ParameterizedType) {
            val rawType = it.rawType
            if (rawType is Class<*> && cls.isAssignableFrom(rawType)) {
                return rawType as Class<T>
            }
        }
    }
    return null
}