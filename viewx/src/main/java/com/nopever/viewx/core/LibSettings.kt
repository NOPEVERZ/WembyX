package com.nopever.viewx.core

import android.content.Context
import com.nopever.viewx.utils.appContextX

/**
 * 一些可能用到的的sp属性
 */
object LibSettings {
    private const val PREF_NAME = "viewx_config"
    private const val SHOW_GUIDE = "show_guide"

    // 使用 lazy 懒加载：只有第一次用到时才会去调用 getApp()
    // 这样能确保 Manager 初始化完成后再获取实例
    private val prefs by lazy {
        appContextX.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 获取/设置 引导页Boolean 值
     *
     * 方便展示引导页
     * if (LibSettings.showGuide) {
     *     LibSettings.showGuide = false
     * }
     */
    var showGuide: Boolean
        get() = prefs.getBoolean(SHOW_GUIDE, true)
        set(value) = prefs.edit().putBoolean(SHOW_GUIDE, value).apply()

    /**
     * 清除所有数据
     */
    fun clear() {
        prefs.edit().clear().apply()
    }
}