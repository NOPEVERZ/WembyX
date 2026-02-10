package com.nopever.z.data.bean

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

data class TestFeature(
    val name: String,
    // 直接持有类的引用，限制为 Fragment 的子类，类型更安全
    val fragmentClass: KClass<out Fragment>? = null,
    val activityClass: KClass<out Activity>? = null,

    // 定义一个点击后的扩展行为，把 Context 传进去以便使用
    val action: ((Context) -> Unit)? = null
)
