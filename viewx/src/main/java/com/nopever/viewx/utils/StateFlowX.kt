package com.nopever.viewx.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

// 切换 (可以在任何线程调用，绝对安全)
fun MutableStateFlow<Boolean>.toggle() {
    this.update { !it }
}