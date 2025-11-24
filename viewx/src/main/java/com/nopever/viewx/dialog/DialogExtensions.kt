package com.nopever.viewx.dialog

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * 自动解析 FragmentManager
 * 支持 Activity 和 Fragment，如果是 Fragment 则默认使用 childFragmentManager
 */
val LifecycleOwner.safeFragmentManager: FragmentManager
    get() = when (this) {
        is FragmentActivity -> this.supportFragmentManager
        is Fragment -> this.childFragmentManager
        else -> throw IllegalArgumentException("Dialog must be shown from FragmentActivity or Fragment, but was ${this::class.java.simpleName}")
    }

// ----------------- 普通 View 扩展 -----------------

/**
 * 快速显示一个中间弹窗
 */
fun LifecycleOwner.showDialog(
    layoutId: Int,
    builder: (EasyDialog.() -> Unit)? = null,
    convert: (holder: DialogViewHolder, dialog: UniversalDialogFragment) -> Unit
) {
    EasyDialog.build(this.safeFragmentManager)
        .setLayout(layoutId)
        .setWidthScale(0.8f) // 默认宽度 80%
        .apply { builder?.invoke(this) }
        .setConvertListener(convert)
        .show()
}

// ----------------- DataBinding / ViewBinding 极简扩展 -----------------

/**
 * 快速显示 BindDialog (无需 LayoutId)
 * 使用方法: showBindDialog<DialogHintBinding> { binding, dialog -> ... }
 */
inline fun <reified T : ViewBinding> LifecycleOwner.showBindDialog(
    noinline builder: (EasyDialog.() -> Unit)? = null,
    noinline convert: (binding: T, dialog: UniversalDialogFragment) -> Unit
) {
    EasyDialog.build(this.safeFragmentManager)
        .setBinding(T::class.java) // 传入 Class，内部提取 name 用于反射
        .setWidthScale(0.8f)
        .apply { builder?.invoke(this) }
        .setBindingListener<T>(convert)
        .show()
}

/**
 * 快速显示 BottomSheetBindDialog (无需 LayoutId)
 */
inline fun <reified T : ViewBinding> LifecycleOwner.showBottomBindDialog(
    noinline builder: (EasyDialog.() -> Unit)? = null,
    noinline convert: (binding: T, dialog: UniversalDialogFragment) -> Unit
) {
    EasyDialog.build(this.safeFragmentManager)
        .setBinding(T::class.java)
        .asBottomSheet()
        .apply { builder?.invoke(this) }
        .setBindingListener<T>(convert)
        .show()
}

/**
 * 显示在某个 View 下方的下拉弹窗 (DataBinding)
 */
inline fun <reified T : ViewBinding> LifecycleOwner.showDropDown(
    anchor: View,
    noinline builder: (EasyDialog.() -> Unit)? = null,
    noinline convert: (binding: T, dialog: UniversalDialogFragment) -> Unit
) {
    EasyDialog.build(this.safeFragmentManager)
        .setBinding(T::class.java)
        .setAnchor(anchor, matchWidth = true) // 默认宽度对齐 anchor
        .apply { builder?.invoke(this) }
        .setBindingListener<T>(convert)
        .show()
}