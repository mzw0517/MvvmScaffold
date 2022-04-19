package com.binding.mvvm.ui.page.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.binding.mvvm.ui.page.config.Config
import com.binding.mvvm.ui.page.config.Config.SCREEN_ORIENTATION
import com.binding.mvvm.ui.statusbar.setStatusBarColor
import com.binding.mvvm.ui.statusbar.setStatusBarLightMode

abstract class BaseActivity : DataBindingActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = SCREEN_ORIENTATION
        super.onCreate(savedInstanceState)
        setStatusBarColor(Color.TRANSPARENT)
        setStatusBarLightMode(isLightMode() ?: Config.lightMode)
    }

    protected open fun isLightMode(): Boolean? = null

}