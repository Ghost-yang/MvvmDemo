package com.example.mvvmdemo.base.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
class AppViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (BaseAndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(Application::class.java).newInstance(application)
            } catch (e: NoSuchMethodException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw IllegalStateException("Cannot create an instance of $modelClass", e)
            }
        } else {
            super.create(modelClass)
        }
    }
}