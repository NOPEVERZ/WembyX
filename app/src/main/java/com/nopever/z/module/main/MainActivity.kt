package com.nopever.z.module.main

import android.os.Bundle
import com.nopever.z.databinding.ActivityMainBinding
import com.nopever.z.module.base.BaseVmActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseVmActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}