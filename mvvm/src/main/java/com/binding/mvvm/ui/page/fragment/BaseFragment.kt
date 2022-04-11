package com.binding.mvvm.ui.page.fragment

import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import com.binding.mvvm.ui.page.config.Config

abstract class BaseFragment : DataBindingFragment() {

    companion object {
        private val HANDLER = Handler(Looper.getMainLooper())
    }

    private var mAnimationLoaded = false

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {

        HANDLER.postDelayed({
            if (!mAnimationLoaded) {
                mAnimationLoaded = true
                loadInitData()
            }
        }, Config.delayMillis)

        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    protected open fun loadInitData() {

    }

}