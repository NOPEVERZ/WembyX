package com.nopever.z.module.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

open class MyBaseViewModel(app: Application) : AndroidViewModel(app) {

    // Loading 状态，UI 层可以观察这个 Flow
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 1. 用于记录带有“唯一标识”的正在运行的任务
    private val activeJobs = mutableMapOf<String, Job>()

    // 引用计数器，防止并发请求导致 Loading 闪烁, 执行完最后一个再结束loading
    private var loadingCount = 0

    // ---- 对外公开方法 ----
    /**
     * 方法 1: 带 Loading 圈的执行方法
     * 适用于：页面初次加载、提交表单等需要用户等待的操作
     * @param cancelKey 如果传入相同的 key，上一个未完成的任务会被取消（防抖/覆盖）
     */
    fun <T> runWithLoading(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        cancelKey: String? = null,
        block: suspend () -> T
    ) = launchRequest(dispatcher, true, cancelKey, block)

    /**
     * 方法 2: 不带 Loading 圈的执行方法
     * 适用于：下拉刷新、静默更新数据、预加载等不需要打断用户的操作
     */
    fun <T> runNoLoading(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        cancelKey: String? = null,
        block: suspend () -> T
    ) = launchRequest(dispatcher, false, cancelKey, block)

    // ---- 核心实现 ----
    private fun <T> launchRequest(
        dispatcher: CoroutineDispatcher,
        showLoading: Boolean,
        cancelKey: String?,
        block: suspend () -> T
    ): AsyncJob<T> {
        val asyncJob = AsyncJob<T>()

        // 2. 如果存在相同 Key 的任务，先取消旧的
        if (cancelKey != null) {
            activeJobs[cancelKey]?.cancel()
        }

        val job = viewModelScope.launch {
            try {
                if (showLoading) updateLoadingCount(1)

                val result = withContext(dispatcher) {
                    block()
                }
                asyncJob.dispatchSuccess(result)

            } catch (e: Exception) {
                // 3. 关键点：如果是被手动/自动取消的，不要触发 onError，直接抛出即可
                if (e is CancellationException) {
                    throw e
                }
                // 其他真正的错误才回调 onError
                asyncJob.dispatchError(e)
            } finally {
                if (showLoading) updateLoadingCount(-1)

                // 任务结束，从 Map 中移除
                if (cancelKey != null) {
                    activeJobs.remove(cancelKey)
                }
            }
        }

        // 记录新任务
        if (cancelKey != null) {
            activeJobs[cancelKey] = job
        }

        return asyncJob
    }

    private fun updateLoadingCount(delta: Int) {
        loadingCount += delta
        _isLoading.value = loadingCount > 0
    }

    fun loading(loading: Boolean) {
        _isLoading.value = loading
    }

//  示例用法
//    fun searchUser(query: String) {
//        // 只要 cancelKey 相同，前面的请求就会被自动取消
//        runNoLoading(cancelKey = "SEARCH_JOB") {
//            api.search(query)
//        }.onSuccess {
//            // 只有最后一次输入的搜索结果会走到这里
//            resultList.value = it
//        }
//    }
}