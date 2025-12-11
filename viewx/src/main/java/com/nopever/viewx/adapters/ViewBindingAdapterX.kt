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
