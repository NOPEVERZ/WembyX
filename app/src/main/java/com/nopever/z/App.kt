package com.nopever.z

import android.app.Application
import com.nopever.viewx.utils.CrashUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashUtil.init(this)
    }
}