package com.nopever.z.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.nopever.z.module.arch.FragmentAttachActivity
import com.nopever.z.module.reuslt.GhostResultFragment

/**
 * 核心方法：启动 Activity 并通过 Lambda 获取结果
 */
fun LifecycleOwner.startActivityForResult(
    intent: Intent,
    options: Bundle? = null,
    callback: (ActivityResult) -> Unit
) {
    val fragmentManager = when (this) {
        is FragmentActivity -> supportFragmentManager
        is Fragment -> childFragmentManager
        else -> throw IllegalArgumentException("LifecycleOwner 必须是 Activity 或 Fragment")
    }

    // 生成一个唯一的 Tag
    val tag = "GhostResult_${System.currentTimeMillis()}"

    val ghostFragment = GhostResultFragment().apply {
        this.targetIntent = intent
        this.options = options
        this.callback = callback
    }

    // 提交事务，添加 Fragment
    fragmentManager.beginTransaction()
        .add(ghostFragment, tag)
        .commitAllowingStateLoss() // 使用 allowStateLoss 防止某些极端情况崩溃
}

/**
 * 便捷方法：启动 Fragment 并获取结果
 */
inline fun <reified T : Fragment> LifecycleOwner.startFragmentForResult(
    args: Bundle? = null,
    options: Bundle? = null,
    noinline callback: (ActivityResult) -> Unit // noinline 允许 lambda 被传递
) {
    // 复用之前的 intentFor 逻辑，或者直接构建 Intent
    val context = when (this) {
        is Fragment -> requireContext()
        is Activity -> this
        else -> throw IllegalStateException("Context not found")
    }

    val intent = Intent(context, FragmentAttachActivity::class.java).apply {
        putExtra(FragmentAttachActivity.EXTRA_CLASS_NAME, T::class.java.name)
        if (args != null) putExtras(args)
    }

    startActivityForResult(intent, options, callback)
}

/**
 * 方式一：Lambda 风格 (推荐，灵活)
 * 用法: finishWithResult { putExtra("key", "value") }
 */
fun FragmentActivity.finishWithResult(
    resultCode: Int = Activity.RESULT_OK,
    block: Intent.() -> Unit = {}
) {
    val intent = Intent().apply(block)
    setResult(resultCode, intent)
    finish()
}

/**
 * 方式二：键值对风格 (最简洁)
 * 用法: finishWithResult("key" to "value", "id" to 123)
 */
//fun FragmentActivity.finishWithResult(
//    vararg params: Pair<String, Any?>,
//    resultCode: Int = Activity.RESULT_OK
//) {
//    val intent = Intent().apply {
//        params.forEach { (key, value) ->
//            when (value) {
//                null -> {} // 忽略 null
//                is Int -> putExtra(key, value)
//                is String -> putExtra(key, value)
//                is Boolean -> putExtra(key, value)
//                is Long -> putExtra(key, value)
//                is Double -> putExtra(key, value)
//                is Float -> putExtra(key, value)
//                is java.io.Serializable -> putExtra(key, value)
//                // 需要其他类型可以在这里补充
//                else -> throw IllegalArgumentException("Unsupported type: ${value::class.java}")
//            }
//        }
//    }
//    setResult(resultCode, intent)
//    finish()
//}