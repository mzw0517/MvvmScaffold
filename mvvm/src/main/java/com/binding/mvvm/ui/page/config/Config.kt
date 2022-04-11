package com.binding.mvvm.ui.page.config

import android.content.pm.ActivityInfo
import android.graphics.Color
import com.binding.mvvm.R

object Config {

    // 屏幕方向 默认竖屏
    var SCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    /**
     * 使用navigation时可配置
     * 例如navigation的动画时间为300ms，则在350毫秒的时候执行加载数据，
     * 可以防止页面卡顿. 可以根据自己的动画进行调整
     */
    var delayMillis: Long = 350

    /**
     * 全局配置toolbar背景色
     *
     * 假如某个页面toolbar背景色不一样
     * 可以在实现方法getToolbarConfig(builder: BaseToolbar.Builder)
     * 通过builder.setBackgroundColor()单独修改
     */
    var toolbarBasicBg = Color.WHITE

    /**
     * 全局配置toolbar标题颜色
     *
     * 假如某个页面toolbar标题颜色不一样
     * 可以在实现方法getToolbarConfig(builder: BaseToolbar.Builder)
     * 通过builder.setTitleTextColor()单独修改
     */
    var toolbarBasicTitleColor = Color.BLACK

    /**
     * 全局配置toolbar返回按钮图标
     *
     * 假如某个页面返回按钮不一样
     * 可以在实现方法getToolbarConfig(builder: BaseToolbar.Builder)
     * 通过builder.setBackButton(R.drawable.xxx)单独修改
     */
    var toolbarBasicNavBackIcon = R.drawable.ic_nav_back_black

    /**
     * 全局配置状态栏文字颜色 true为黑色 false为白色
     *
     * 假如某个页面返回按钮不一样
     * 可以在实现方法getToolbarConfig(builder: BaseToolbar.Builder)
     * 通过builder.setBackButton(R.drawable.xxx)单独修改
     */
    var darkMode = true

}