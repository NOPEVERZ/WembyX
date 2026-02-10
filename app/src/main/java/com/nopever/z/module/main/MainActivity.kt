package com.nopever.z.module.main

import android.os.Bundle
import com.nopever.viewx.utils.statusBarTextColor
import com.nopever.z.databinding.ActivityMainBinding
import com.nopever.z.module.arch.BaseVmActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseVmActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTextColor()
        binding.page = this
    }
}