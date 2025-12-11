package com.nopever.viewx.core

import android.content.Context
import androidx.startup.Initializer

/**
 * 可以禁用，禁用后需要app里手动LibManager.init(context)
 * <meta-data android:name="com.nopever.viewx.manager.LibInitializer" tools:node="remove" />
 */
class LibInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // 自动将 context 传给管理器
        LibManager.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 如果库不依赖其他 Startup 库，返回空列表
        return emptyList()
    }
}