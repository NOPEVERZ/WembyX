package com.nopever.z.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> Class<T>.inflate(layoutInflater: LayoutInflater): T {
    val inflateMethod = this.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    @Suppress("UNCHECKED_CAST")
    return inflateMethod.invoke(null, layoutInflater, null, false) as T
}