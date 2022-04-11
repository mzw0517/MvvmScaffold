package com.binding.mvvm.ui.page.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.binding.mvvm.ui.page.config.Config
import com.binding.mvvm.ui.statusbar.immersive
import com.binding.mvvm.ui.toolbar.BaseToolbar

abstract class ToolbarFragment : BaseFragment() {

    private var mToolbar: BaseToolbar? = null

    abstract fun getToolbarConfig(builder: BaseToolbar.Builder): BaseToolbar.Builder?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val builder = getToolbarConfig(configBasicToolbar())

        return if (builder == null)
            super.onCreateView(inflater, container, savedInstanceState)
        else
            LinearLayout(requireContext()).apply {

                orientation = LinearLayout.VERTICAL

                layoutParams =
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                removeAllViews()

                mToolbar = builder.build().also {
                    addView(it, 0)
                    if (isDarkMode() == null) {
                        requireActivity().immersive(it, Config.darkMode)
                    } else {
                        requireActivity().immersive(it, isDarkMode())
                    }
                }

                addView(super.onCreateView(inflater, container, savedInstanceState), 1)
            }
    }

    private fun configBasicToolbar() = BaseToolbar.Builder(requireActivity())
        .setBackButton(Config.toolbarBasicNavBackIcon)
        .setBackgroundColor(Config.toolbarBasicBg)
        .setTitleTextColor(Config.toolbarBasicTitleColor)

    protected open fun isDarkMode(): Boolean? = null
}