package com.example.mvvmdemo.base.viewmodel

import android.app.Application
import com.example.mvvmdemo.base.repository.BaseRepository

/**
 * @description
 * @author Raymond
 * @date 2023/4/14
 *
 */
abstract class BaseAndroidViewModel<T : BaseRepository>(var application: Application) :
    BaseViewModel<T>()