package com.nopever.z.module.pb

import android.content.Context
import android.os.Bundle
import android.view.View
import com.nopever.z.databinding.FragmentPbBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.nopever.z.module.arch.startFragment
import com.nopever.z.module.base.MyBaseFragment

@AndroidEntryPoint
class PbFragment : MyBaseFragment<FragmentPbBinding, PbViewModel>() {

    companion object {
        fun start(context: Context) {
            context.startFragment<PbFragment>()
        }
    }

    override val mViewModel: PbViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.page = this
        binding.viewModel = mViewModel
    }
}