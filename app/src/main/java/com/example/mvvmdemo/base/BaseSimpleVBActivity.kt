package com.example.composedemo.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.expand.findActualGenericsClass
import java.lang.reflect.InvocationTargetException

/**
 * @description 简单逻辑activity封装
 * @author Raymond
 * @date 2023/4/11
 *
 */
abstract class BaseSimpleVBActivity<VB : ViewBinding> : ComponentActivity() {
    protected val binding by lazy {
        createViewBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    open fun createViewBinding(): VB {
        val actualGenericsClass = findActualGenericsClass<VB>(ViewBinding::class.java)
            ?: throw NullPointerException("Can not find a ViewBinding Generics in ${javaClass.simpleName}")
        try {
            val inflate =
                actualGenericsClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return inflate.invoke(null, layoutInflater) as VB
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw throw NullPointerException("create viewbinding failed ${javaClass.simpleName}")
    }
}



