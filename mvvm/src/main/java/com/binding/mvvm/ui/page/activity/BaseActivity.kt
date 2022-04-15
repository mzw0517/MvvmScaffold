package com.binding.mvvm.ui.page.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.binding.mvvm.ui.page.config.Config
import com.binding.mvvm.ui.page.config.Config.SCREEN_ORIENTATION
import com.binding.mvvm.ui.statusbar.darkMode
import com.binding.mvvm.ui.statusbar.immersive

abstract class BaseActivity : DataBindingActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = SCREEN_ORIENTATION
        super.onCreate(savedInstanceState)
        immersive()
        darkMode(isDarkMode() ?: Config.darkMode)
    }

    protected open fun isDarkMode(): Boolean? = null

}