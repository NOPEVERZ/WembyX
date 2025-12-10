package com.nopever.viewx.manager

import android.app.Application
import android.content.Context
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

object LibManager {
    private const val TAG = "LibManager"
    private var app: Application? = null

    // 使用 AtomicBoolean 防止在多线程环境下多次执行初始化逻辑
    private val isInitialized = AtomicBoolean(false)

    /**
     * 初始化方法
     * 既可以被 App Startup 自动调用，也可以被宿主手动调用
     */
    fun init(context: Context) {
        if (isInitialized.getAndSet(true)) {
            Log.w(TAG, "ViewX Library 已经初始化过了，忽略本次调用。")
            return
        }

        // 核心防御：无论传入的是 Activity 还是 Service，强制转为 Application Context
        // 防止持有 Activity 导致内存泄漏
        this.app = context.applicationContext as Application

//        Log.d(TAG, "ViewX Library 初始化成功！Context: $app")
        Log.e(TAG, "ViewX Library 初始化成功！")

        // 在这里执行你其他的初始化逻辑，比如初始化数据库、SDK配置等
        // doOtherSetup()
    }

    /**
     * 获取 Application 对象
     * 如果未初始化则抛出异常，快速失败
     */
    fun getApp(): Application {
        return app ?: throw IllegalStateException(
            "ViewX Library 尚未初始化！\n" +
                    "如果使用了 tools:node=\"remove\" 禁用了自动初始化，" +
                    "请务必在 Application.onCreate() 中手动调用 LibManager.init(this)"
        )
    }
}