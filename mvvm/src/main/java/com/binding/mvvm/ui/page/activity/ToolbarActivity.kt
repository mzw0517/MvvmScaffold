package com.binding.mvvm.ui.page.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.binding.mvvm.ui.page.config.Config
import com.binding.mvvm.ui.toolbar.BaseToolbar

abstract class ToolbarActivity : BaseActivity() {

    private var mToolbar: BaseToolbar? = null

    abstract fun getToolbarConfig(builder: BaseToolbar.Builder): BaseToolbar.Builder?

    override fun onCreate(savedInstanceState: Bundle?) {

        val builder = getToolbarConfig(configBasicToolbar())

        if (builder == null) {
            super.onCreate(savedInstanceState)
        } else {
            LinearLayout(this).apply {

                orientation = LinearLayout.VERTICAL

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                removeAllViews()

                mToolbar = builder.build().also {
                    addView(it, 0)
                }

                super.onCreate(savedInstanceState)

                getBinding()?.root?.also {

                    val viewGroup = it.parent as ViewGroup

                    viewGroup.removeView(it)

                    addView(it, 1)

                    viewGroup.addView(this)
                }

                setSupportActionBar(mToolbar)
            }
        }
    }

    private fun configBasicToolbar(): BaseToolbar.Builder {
        val builder = BaseToolbar.Builder(this)
            .setBackButton(Config.toolbarBasicNavBackIcon)
            .setBackgroundColor(Config.toolbarBasicBg)
            .setTitleTextColor(Config.toolbarBasicTitleColor)

        if (Config.toolbarNeedFakeStatusBar) {
            builder.setStatusBarColor(Config.toolbarStatusBarColor)
        }
        return builder
    }

}