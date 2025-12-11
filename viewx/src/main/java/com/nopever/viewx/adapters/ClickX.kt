package com.nopever.viewx.adapters

import android.os.SystemClock
import android.view.View
import androidx.databinding.BindingAdapter
import com.nopever.viewx.R

fun View.onClickX(wait: Long = 300, block: (() -> Unit)) {
    setOnClickListener(throttleClickNoView(wait, block))
}

fun throttleClickNoView(wait: Long = 300, block: (() -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        val current = SystemClock.uptimeMillis()
        val lastClickTime = (v.getTag(R.id.qmui_click_timestamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.qmui_click_timestamp, current)
            block()
        }
    }
}

/**
 * 防抖点击, 移除onClick的View参数
 * @param callback 点击事件
 * @param interval 点击间隔时间
 */
@BindingAdapter("onClickX", "clickInterval", requireAll = false)
fun bindThrottleClickX(view: View, callback: (() -> Unit)?, interval: Int? = 300) {
    callback?.let { view.onClickX(interval?.toLong() ?: 300, it) }
}