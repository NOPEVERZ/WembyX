package com.nopever.viewx.utils

import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

/**
 * 去除 TabLayout 长按显示文本的 Toast
 */
fun TabLayout.removeLongClickToast() {
    // 获取内部的 SlidingTabStrip
    val tabStrip = this.getChildAt(0) as? ViewGroup ?: return

    // 遍历所有的 Tab View
    for (i in 0 until tabStrip.childCount) {
        val tabView = tabStrip.getChildAt(i)
        // 设置长按监听器并返回 true，消费事件，阻止 Toast 弹出
        tabView.setOnLongClickListener { true }

        // 可选：如果针对 Android 8.0+ (API 26+) 也可以显式置空 tooltip
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            tabView.tooltipText = null
        }
    }
}