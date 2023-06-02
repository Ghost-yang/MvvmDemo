package com.example.mvvmdemo.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow

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

fun View.clickThrottle(blok: (View) -> Unit) {
    val intervalTime = 500
    var lastTime = 0L
    setOnClickListener {
        val currentime = System.currentTimeMillis()
        if (currentime - lastTime > intervalTime) {
            lastTime = currentime
            blok(it)
        }
    }
}

/**
 * flow流式防抖
 *
 * @return
 */
fun View.clickFlow(): Flow<View> {
    return callbackFlow {
        setOnClickListener {
            trySend(it)
        }
        awaitClose { setOnClickListener(null) }
    }.throttleFirst(500)

}

fun View.clickFlow(lifecycleCoroutineScope: LifecycleCoroutineScope, blok: () -> Unit) {
    callbackFlow {
        setOnClickListener {
            trySend(it)
        }
        awaitClose { setOnClickListener(null) }
    }.throttleFirst(500)

}

/**
 * 搜索框防抖
 *
 * @return
 */
@OptIn(FlowPreview::class)
fun EditText.textChange(): Flow<Editable?> {
    return callbackFlow<Editable?> {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                trySend(p0)
            }

        }
        addTextChangedListener(watcher)

        awaitClose {
            removeTextChangedListener(watcher)
        }
    }.debounce(300)
}

fun <T> Flow<T>.throttleFirst(thresholdMillis: Long): Flow<T> = flow {
    var lastTime = 0L
    collect {
        // 当前时间
        val currentTime = System.currentTimeMillis()
        // 时间差超过阈值则发送数据并记录时间
        if (currentTime - lastTime > thresholdMillis) {
            lastTime = currentTime
            emit(it)
        }
    }
}
