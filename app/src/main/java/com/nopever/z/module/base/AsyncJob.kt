package com.nopever.z.module.base

// 1. 定义一个用于处理链式调用的辅助类
class AsyncJob<T> {
    private var successBlock: ((T) -> Unit)? = null
    private var errorBlock: ((Throwable) -> Unit)? = null
    private var finallyBlock: (() -> Unit)? = null

    // 设置成功回调
    fun onSuccess(block: (T) -> Unit): AsyncJob<T> {
        successBlock = block
        return this
    }

    // 设置失败回调
    fun onError(block: (Throwable) -> Unit): AsyncJob<T> {
        errorBlock = block
        return this
    }

    // 可选：设置结束回调
    fun onFinally(block: () -> Unit): AsyncJob<T> {
        finallyBlock = block
        return this
    }

    // 内部方法：由 BaseViewModel 调用以触发回调
    fun dispatchSuccess(data: T) {
        successBlock?.invoke(data)
        finallyBlock?.invoke()
    }

    fun dispatchError(throwable: Throwable) {
        errorBlock?.invoke(throwable)
        finallyBlock?.invoke()
    }
}