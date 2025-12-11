package com.nopever.viewx.adapters

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isShow")
fun isShow(view: View, show: Boolean) {
    view.isVisible = show
}

@BindingAdapter("isVisible")
fun isVisible(view: View, show: Boolean) {
    view.isInvisible = !show
}

@BindingAdapter("isGone")
fun isGone(view: View, gone: Boolean) {
    view.isGone = gone
}

@BindingAdapter("clickX", "clickInterval", requireAll = false)
fun clickX(view: View, action: OnClickActionX?, interval: Int? = 300) {
    action?.let { listener ->
        // 将接口桥接到你的扩展方法中
        view.onClickX(interval?.toLong() ?: 300) {
            listener.onAction()
        }
    }
}

@BindingAdapter("clickXTest")
fun clickXTest(view: View, action: OnClickActionX?) {
    action?.let { listener ->
        // 将接口桥接到你的扩展方法中
        view.onClickX(300) {
            listener.onAction()
        }
    }
}


@BindingAdapter("onClickVisibleTest")
fun onClickVisibleTest(view: View, show: Boolean) {
    view.isInvisible = !show
}

