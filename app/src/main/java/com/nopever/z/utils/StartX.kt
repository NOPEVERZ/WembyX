package com.nopever.z.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

// 安全获取 Context
// 不再强行反射 LifecycleOwner，而是针对具体类型扩展
val Fragment.safeContext: Context
    get() = requireContext()

val View.safeContext: Context
    get() = context

// 简化的 Intent 创建 (保留你喜欢的 reified 写法)
inline fun <reified T : Activity> Context.intentFor(
    vararg params: Pair<String, Any?>
): Intent {
    return Intent(this, T::class.java).apply {
        // 这里可以配合 core-ktx 的 bundleOf，或者简单的 putExtra
        // 为保持简单，这里暂不引入 extra 库，如有需要可自行扩展
    }
}

// 启动 Activity 的终极简化写法
inline fun <reified T : Activity> Activity.start(
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java).apply(block)
    startActivity(intent, options)
}

inline fun <reified T : Activity> Fragment.start(
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    requireContext().startActivity(Intent(requireContext(), T::class.java).apply(block), options)
}

inline fun <reified T : Activity> View.start(
    options: Bundle? = null,
    block: Intent.() -> Unit = {}
) {
    context.startActivity(Intent(context, T::class.java).apply(block), options)
}

// 注意：原代码中的 registerActivityResult 写法在 Activity Result API 中不再推荐动态调用。
// 建议在界面初始化时使用 registerForActivityResult。