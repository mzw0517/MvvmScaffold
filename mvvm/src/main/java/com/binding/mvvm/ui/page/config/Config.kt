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

    // toolbar背景色
    var toolbarBasicBg = Color.WHITE

    // toolbar标题颜色
    var toolbarBasicTitleColor = Color.BLACK

    // toolbar返回按钮图标
    var toolbarBasicNavBackIcon = R.drawable.ic_nav_back_black

}