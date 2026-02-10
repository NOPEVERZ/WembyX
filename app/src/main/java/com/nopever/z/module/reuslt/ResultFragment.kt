package com.nopever.z.module.reuslt

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import com.nopever.viewx.utils.statusBarTextColor
import com.nopever.z.databinding.FragmentResultBinding
import com.nopever.z.module.arch.BaseVmFragment
import com.nopever.z.utils.finishWithResult
import com.nopever.z.utils.startFragmentForResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : BaseVmFragment<FragmentResultBinding>() {

    companion object {
        fun start(lifecycleOwner: LifecycleOwner, onResult: (ActivityResult) -> Unit) {
            lifecycleOwner.startFragmentForResult<ResultFragment>(
                callback = onResult
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusBarTextColor()
        binding.page = this
    }

    fun onClickFinish() {
        activity?.finishWithResult {
            putExtra("result", "返回结果数据")
//            action = "com.my.app.ACTION_RETURN"
        }
    }
}