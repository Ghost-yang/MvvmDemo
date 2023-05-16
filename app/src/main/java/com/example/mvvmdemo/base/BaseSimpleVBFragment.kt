package com.example.mvvmdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.expand.findActualGenericsClass
import java.lang.reflect.InvocationTargetException

/**
 * @description
 * @author Raymond
 * @date 2023/4/11
 *
 */
abstract class BaseSimpleVBFragment<VB : ViewBinding> : Fragment() {
    protected val binding get() = _binding
    private var _binding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(container)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createViewBinding(parent: ViewGroup?): VB {
        val actualGenericsClass = findActualGenericsClass<VB>(ViewBinding::class.java)
            ?: throw NullPointerException("Can not find a ViewBinding Generics in ${javaClass.simpleName}")
        try {
            val inflate =
                actualGenericsClass.getDeclaredMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
            return inflate.invoke(null, layoutInflater, parent, false) as VB
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