package com.nopever.z.module.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.nopever.z.databinding.ActivitySplashBinding
import com.nopever.z.module.base.BaseVmActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : BaseVmActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}