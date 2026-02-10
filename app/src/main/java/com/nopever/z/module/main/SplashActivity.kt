package com.nopever.z.module.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.nopever.viewx.utils.statusBarTextColor
import com.nopever.z.databinding.ActivitySplashBinding
import com.nopever.z.module.arch.BaseVmActivity
import com.nopever.z.utils.start
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseVmActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTextColor()
        lifecycleScope.launch {
            delay(1000)
            start<MainActivity>()
            finish()
        }
    }
}