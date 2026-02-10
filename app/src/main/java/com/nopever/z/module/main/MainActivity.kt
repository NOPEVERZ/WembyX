package com.nopever.z.module.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nopever.viewx.activity.CrashActivity
import com.nopever.viewx.utils.statusBarTextColor
import com.nopever.z.R
import com.nopever.z.data.bean.TestFeature
import com.nopever.z.databinding.ActivityMainBinding
import com.nopever.z.module.arch.BaseVmActivity
import com.nopever.z.module.arch.FragmentAttachActivity
import com.nopever.z.module.dialog.DialogTestFragment
import com.nopever.z.module.pb.PbFragment
import com.nopever.z.module.reuslt.ResultFragment
import com.nopever.z.widget.setupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class MainActivity : BaseVmActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBarTextColor()
        binding.page = this
        initRV()
    }

    private val list = listOf(
        TestFeature("Dialog", DialogTestFragment::class),
        TestFeature("ProgressBar", PbFragment::class),
        TestFeature("CrashActivity", activityClass = CrashActivity::class),
        TestFeature("Fragment Result") {
            ResultFragment.start(this) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data?.getStringExtra("result")
                    Toast.makeText(it, data, Toast.LENGTH_SHORT).show()
                }
            }
        },

        TestFeature("Toast") {
            Toast.makeText(it, "i'm toast.", Toast.LENGTH_SHORT).show()
        },
    )

    private fun initRV() {
        binding.rvFeature.setupAdapter<TestFeature>(R.layout.item_feature) {
            setOnItemClickListener { _, item, _ ->
                item.fragmentClass?.let {
                    startFeatureFragment(item.fragmentClass)
                } ?: item.activityClass?.let {
                    startFeatureActivity(item.activityClass)
                } ?: item.action?.invoke(this@MainActivity)
            }
        }.submitList(list)
    }

    private fun startFeatureFragment(kClass: KClass<out Fragment>) {
        val intent = Intent(this, FragmentAttachActivity::class.java).apply {
            putExtra(FragmentAttachActivity.EXTRA_CLASS_NAME, kClass.java.name)
        }
        startActivity(intent)
    }

    private fun startFeatureActivity(kClass: KClass<out Activity>) {
        startActivity(Intent(this, kClass.java))
    }
}