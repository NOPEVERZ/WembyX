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
 * @param action 点击事件
 * @param interval 点击间隔时间
 */
@BindingAdapter("onClickX", "clickInterval")
fun bindThrottleClickX(view: View, action: OnClickActionX?, interval: Int) {
    action?.let { listener ->
        // 将接口桥接到你的扩展方法中
        view.onClickX(interval.toLong()) {
            listener.onAction()
        }
    }
}

@BindingAdapter("onClickX")
fun bindThrottleClickXNoInterval(view: View, action: OnClickActionX?) {
    action?.let { listener ->
        // 这里硬编码默认值 300
        view.onClickX(300) { listener.onAction() }
    }
}



/**
 * 一个不带参数的点击回调接口，专门用于 DataBinding
 */
fun interface OnClickActionX {
    fun onAction()
}

//@BindingAdapter("onClickX", "clickInterval", requireAll = false)
//fun bindThrottleClickX(view: View, callback: (() -> Unit)?, interval: Int? = 300) {
//    callback?.let { view.onClickX(interval?.toLong() ?: 300, it) }
//}


//@BindingAdapter("onClickX", "clickInterval", requireAll = false)
//fun bindThrottleClickX(view: View, listener: View.OnClickListener?, interval: Int? = 300) {
//    listener?.let {
//        view.onClickX(interval?.toLong() ?: 300) {
//            it.onClick(view)
//        }
//    }
//}

