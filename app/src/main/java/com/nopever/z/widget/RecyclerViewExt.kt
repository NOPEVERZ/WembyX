package com.nopever.z.widget

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nopever.z.BR

inline fun <reified T : Any> RecyclerView.setupAdapter(
    @LayoutRes layoutResId: Int,
    variableId: Int = BR.item,
    extraBindings: Map<Int, Any> = emptyMap(),
    clickIds: IntArray = intArrayOf(),
    longClickIds: IntArray = intArrayOf(),
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback(),
    block: QuickBindingAdapter<T>.() -> Unit
): QuickBindingAdapter<T> {

    val adapter = QuickBindingAdapter(
        layoutResId = layoutResId,
        variableId = variableId,
        extraBindings = extraBindings,
        clickIds = clickIds,
        longClickIds = longClickIds,
        diffCallback = diffCallback
    )

    adapter.block() // 执行回调设置
    this.adapter = adapter
    return adapter
}