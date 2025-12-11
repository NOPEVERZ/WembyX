package com.nopever.viewx.utils

import androidx.lifecycle.MutableLiveData

/**
 * 切换 MutableLiveData<Boolean> 的值。
 *
 * 规则:
 * - true  -> false
 * - false -> true
 * - null  -> true (将 null 视作 false)
 *
 * **注意：** 此方法使用 `setValue()`，必须在UI主线程上调用。
 */
fun MutableLiveData<Boolean>.toggle() {
    // 1. 获取当前值，如果为 null，则默认为 false
    val currentValue = this.value ?: false

    // 2. 设置为相反的值
    this.value = !currentValue
}

/**
 * 切换 MutableLiveData<Boolean> 的值（线程安全）。
 * 不一定线程安全
 * 此方法使用 `postValue()`，可以从任何线程调用。
 * 它会异步地在主线程上更新值。
 */
fun MutableLiveData<Boolean>.togglePost() {
    val currentValue = this.value ?: false
    this.postValue(!currentValue)
}

@JvmName("togglePostNullable")
fun MutableLiveData<Boolean?>.togglePost() {
    val currentValue = this.value ?: false
    this.postValue(!currentValue)
}

/**
 * 为可空 Boolean
 */
@JvmName("toggleNullableBoolean")
fun MutableLiveData<Boolean?>.toggle() {
    this.value = this.value?.not()
}