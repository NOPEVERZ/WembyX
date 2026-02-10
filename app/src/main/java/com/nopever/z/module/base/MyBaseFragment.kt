package com.nopever.z.module.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nopever.viewx.utils.statusBarTextColor
import com.nopever.z.module.arch.BaseVmFragment
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.launch

abstract class MyBaseFragment<VB: ViewDataBinding, VM : MyBaseViewModel> : BaseVmFragment<VB>() {

    protected abstract val mViewModel: VM

    private var loadingDialog: QMUITipDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.statusBarTextColor()

        // 初始化 Loading Dialog
        initLoadingDialog()

        // 观察 ViewModel 的 Loading 状态
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.isLoading.collect { isLoading ->
                    if (isLoading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = QMUITipDialog.Builder(requireContext())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中...")
                .create(false)
        }
    }

    private fun showLoading() {
        if (loadingDialog?.isShowing == false && isAdded) {
            loadingDialog?.show()
        }
    }

    private fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.dismiss()
        loadingDialog = null
    }
}