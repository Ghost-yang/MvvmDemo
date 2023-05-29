package com.example.mvvmdemo.utils

import android.view.View
import android.view.View.OnClickListener

/**
 * @description
 * @author Raymond
 * @date 2023/5/29
 *
 */

fun View.click(blok: () -> Unit) {
    val listener by lazy {
        OnClickListener {
            blok()
        }
    }
    setOnClickListener(listener)
}
