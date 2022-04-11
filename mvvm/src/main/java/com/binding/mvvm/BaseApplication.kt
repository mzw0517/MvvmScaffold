package com.binding.mvvm

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class BaseApplication : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
    }

    override fun getViewModelStore() = mAppViewModelStore
}