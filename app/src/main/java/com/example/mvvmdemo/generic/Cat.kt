package com.example.mvvmdemo.generic

import java.lang.reflect.ParameterizedType

/**
 * @description
 * @author Raymond
 * @date 2023/4/12
 *
 */
class Cat : Animal<String, Int>() {
}

fun main(args: Array<String>) {
    println("Cat-genericSuperclass-${Cat::class.java.genericSuperclass}")
    println("Cat-genericSuperclass-${Cat::class.java.superclass}")
    println("Animal-genericSuperclass-${Animal::class.java.genericSuperclass}")
    println("Animal-genericSuperclass-${Animal::class.java.superclass}")

    val type = Cat::class.java.genericSuperclass
    val params = type as ParameterizedType
    params.actualTypeArguments.size
}