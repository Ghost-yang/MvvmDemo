package com.example.mvvmdemo.expand

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding

/**
 * @description
 * @author Raymond
 * @date 2023/4/12
 *
 */
//使用反射的代码如下：
//使用反射获取inflate方法
inline fun <reified VB : ViewBinding> ComponentActivity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).also { binding ->
        setContentView(binding.root)
        //  if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB

//不使用反射的代码如下:
//将inflate函数作为参数传递
fun <VB : ViewBinding> ComponentActivity.inflate(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).also { binding ->
        setContentView(binding.root)
        // if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}