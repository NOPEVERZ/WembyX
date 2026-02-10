package com.nopever.z.module.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.nopever.z.databinding.FragmentDialogTestBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.nopever.viewx.dialog.EasyDialog
import com.nopever.viewx.dialog.showBindDialog
import com.nopever.viewx.dialog.showBottomBindDialog
import com.nopever.viewx.dialog.showDropDown
import com.nopever.viewx.storage.SpMutableLiveData
import com.nopever.viewx.utils.toggle
import com.nopever.z.R
import com.nopever.z.databinding.DialogHintLayoutBinding
import com.nopever.z.module.arch.startFragment
import com.nopever.z.module.base.MyBaseFragment
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class DialogTestFragment : MyBaseFragment<FragmentDialogTestBinding, DialogTestViewModel>() {

    override val mViewModel: DialogTestViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.page = this
        binding.viewModel = mViewModel

        binding.hl.setOnLeftImageViewClickListener {
            showBindDialog()
        }
        binding.hl.setOnRightImageViewClickListener {
            showCustomConfig()
        }
    }

    fun onClickDialog() {
        startFragment<DialogTestFragment>()
        // 带参数跳转
        // val args = Bundle().apply { putString("id", "123") }
        // startFragment<DetailFragment>(args)
    }

    fun showBindDialog() {
        // 在 Activity 中直接调用
        showBindDialog<DialogHintLayoutBinding> { binding, dialog ->
            binding.tvTitle.text = "新架构标题"
            binding.tvContent.text = "这是内容"

            binding.btnConfirm.setOnClickListener {
                // 你的逻辑
                dialog.dismiss()
            }

            binding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun showEasyDialog(view: View) {
        EasyDialog.build(this)
            .setLayout(R.layout.dialog_hint_layout)
            .setWidthScale(0.9f) // 90% 宽度
            .setGravity(Gravity.TOP) // 顶部显示
//            .setAnim(R.style.TopEnterAnim) // 自定义动画
            .setDimAmount(0.3f) // 背景变暗程度
            .setBindingListener<DialogHintLayoutBinding> { binding, dialog ->
                binding.tvTitle.text = "easy easy"

            }
            .show()
    }

    fun showCustomConfig() {
        // 这里的第一个 lambda 就是 builder，用于配置弹窗属性
        showBindDialog<DialogHintLayoutBinding>({
            // 【在这里修改属性】
            setWidthScale(0.9f) // 修改宽度比例，覆盖默认的 0.8f
            setGravity(Gravity.TOP) // 修改位置到顶部
            setDimAmount(0.3f) // 修改背景暗度
//            setAnim(R.style.MyDialogAnim) // 设置自定义动画
            setCanceledOnTouchOutside(false) // 禁止点击外部关闭
            setCanceledOnBackPressed(false)
            setHeightDp(300)
        }) { binding, dialog ->
            // 【这里是 View 处理逻辑】
            binding.tvContent.text = "属性已修改"
            binding.btnConfirm.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun showBottom(view: View) {
        showBottomBindDialog<DialogHintLayoutBinding> { binding, dialog ->
            // 自动拥有 BottomSheet 的拖拽、从底部弹出等特性
        }
    }

    fun onClick(view: View) {
        showPop()
    }

    fun showPop() {
        showDropDown<DialogHintLayoutBinding>(binding.tvTest) { binding, dialog ->
        }
    }

    fun showPop2(view: View) {
        //如果不想让弹窗宽度和按钮一样窄，而是自定义宽度并稍微调整位置：
        showBindDialog<DialogHintLayoutBinding> ({
            // 设置锚点，X 轴不偏移，Y 轴向下偏移 10dp
            setAnchor(binding.pop2, yOff = 20, matchWidth = false, horizontalGravity = Gravity.END)
            // 设置弹窗自己的宽度
            setWidth(60)
            // 设置一点背景阴影
            setDimAmount(0.1f)
        }) { binding, dialog ->
            // 绑定逻辑...
        }
    }

    fun crashTest(view: View) {
        val i = "sssss"
        val str = i.toInt()
    }

    val testsp = SpMutableLiveData<Boolean>("hahah", true)

    val sp = MutableLiveData<Boolean?>()

    val isChecked = MutableStateFlow(false)

    fun spTest() {
        sp.toggle()
        testsp.setValue(!(testsp.value?:false))
    }
}