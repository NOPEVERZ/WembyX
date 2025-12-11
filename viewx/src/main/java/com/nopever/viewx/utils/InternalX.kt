package com.nopever.viewx.utils

import com.nopever.viewx.core.LibManager

// 限制为 internal，防止污染宿主 App 的命名空间
internal val appContextX: android.app.Application
    get() = LibManager.getApp()