package com.example.mvvmdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.mvvmdemo.base.response.DataState
import com.example.mvvmdemo.base.viewmodel.AppViewModelFactory
import com.example.mvvmdemo.base.viewmodel.BaseAndroidViewModel
import com.example.mvvmdemo.base.viewmodel.BaseViewModel
import com.example.mvvmdemo.expand.findActualGenericsClass
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class BaseVMFragment<VM : BaseViewModel<*>, VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding

    val viewModel by lazy {
        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(container)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadlivedata.observe(
            this
        ) { t ->
            when (t.state) {
                DataState.STATE_LOADING -> {
                    showLoading(t.msg)
                }
                DataState.STATE_FINISH -> {
                    dismissLoading()
                }
                else -> {

                }
            }
        }
    }

    /**
     * 显示Loading
     */
    open fun showLoading(msg: String? = null) {
        // ToastUtils.showToast("showLoading")
    }

    /**
     * 隐藏Loading
     */
    open fun dismissLoading() {
        // ToastUtils.showToast("hideLoading")
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


    open fun createViewModel(): VM {
        val actualGenericsClass = findActualGenericsClass<VM>(BaseViewModel::class.java)
            ?: throw NullPointerException("Can not find a ViewModel Generics in ${javaClass.simpleName}")

        if (Modifier.isAbstract(actualGenericsClass.modifiers)) {
            throw IllegalStateException("$actualGenericsClass is an abstract class,abstract ViewModel class can not create a instance!")
        }
        if (BaseAndroidViewModel::class.java.isAssignableFrom(actualGenericsClass)) {
            return ViewModelProvider(
                this,
                AppViewModelFactory(requireActivity().application)
            )[actualGenericsClass]
        }
        return ViewModelProvider(this)[actualGenericsClass]
    }
}