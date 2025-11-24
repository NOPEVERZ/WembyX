package com.nopever.viewx.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialog
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nopever.viewx.data.DialogConfig
import com.nopever.viewx.utils.CommonUtil

class UniversalDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val KEY_CONFIG = "dialog_config"

        fun newInstance(config: DialogConfig): UniversalDialogFragment {
            return UniversalDialogFragment().apply {
                arguments = Bundle().apply { putParcelable(KEY_CONFIG, config) }
            }
        }
    }

    private val config: DialogConfig by lazy {
        arguments?.getParcelable(KEY_CONFIG) ?: DialogConfig()
    }

    // 用来暂存反射创建的 Binding 对象，传给回调
    private var _binding: ViewBinding? = null

    // 回调接口，避免序列化问题，我们在 Builder 中设置
    internal var onBindView: ((DialogViewHolder, UniversalDialogFragment) -> Unit)? = null
    internal var onBindingInflated: ((ViewBinding, UniversalDialogFragment) -> Unit)? = null // 新增：专门给纯 Binding 模式的回调
    internal var onDismissAction: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (config.isBottomSheet) {
            // 只有标记为 BottomSheet 时，才使用底部弹窗特性
            super.onCreateDialog(savedInstanceState)
        } else {
            // 否则返回一个普通的 AppCompatDialog，它支持 Gravity.CENTER 或自定义坐标
            AppCompatDialog(requireContext(), theme)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 方式 1: 使用 layoutId (旧方式)
        if (config.layoutId != 0) {
            return inflater.inflate(config.layoutId, container, false)
        }

        // 方式 2: 使用 Binding Class 反射 (新方式)
        config.bindingClassName?.let { className ->
            try {
                val clazz = Class.forName(className)
                // 获取静态方法 inflate(LayoutInflater, ViewGroup, boolean)
                val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
                _binding = method.invoke(null, inflater, container, false) as ViewBinding
                return _binding?.root
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 处理圆角 (简单处理，复杂需求建议在 xml layout 根布局处理)
        //暂不处理
        if (config.radius > 0 && !config.isBottomSheet) {
            // 这里可以使原本的 View.radius 扩展方法，或者 CardView
            // 建议：让用户在 XML 根布局控制圆角，或者在这里给 View 套一个 Drawable
        }

        // 如果是通过反射创建的 Binding，直接回调 binding 接口
        if (_binding != null) {
            onBindingInflated?.invoke(_binding!!, this)
        } else {
            // 否则走通用 ViewHolder 接口
            val holder = DialogViewHolder(view)
            onBindView?.invoke(holder, this)
        }
    }

    override fun onStart() {
        super.onStart()
        initWindow()
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        onDismissAction?.invoke()
        _binding = null // 防止内存泄漏
    }

    private fun initWindow() {
        val window = dialog?.window ?: return

        // 基础设置 去黑边
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!config.isBottomSheet) {
            // 【普通弹窗 / DropDown 逻辑】
            val params = window.attributes
            val displayMetrics = resources.displayMetrics

            // 宽高设置 (保持不变)
            params.width = if (config.widthScale > 0) (displayMetrics.widthPixels * config.widthScale).toInt() else config.width
            params.height = if (config.heightScale > 0) (displayMetrics.heightPixels * config.heightScale).toInt() else config.height
            params.alpha = config.alpha
            params.dimAmount = config.dimAmount

            // 【新增】处理绝对坐标定位 (类似 PopupWindow)
            if (config.isAbsolutePos) {
                // 强制重置 Gravity 为左上角，这样 X,Y 才能生效
                window.setGravity(Gravity.TOP or Gravity.START)
                params.gravity = Gravity.TOP or Gravity.START

                params.x = config.dialogX
                // 修正 Y 轴坐标：减去状态栏高度，因为 Dialog 窗口通常不覆盖状态栏
                params.y = config.dialogY - CommonUtil.getStatusBarHeight(requireContext())
            } else {
                // 普通居中弹窗
                window.setGravity(config.gravity)
                params.gravity = config.gravity
            }

            window.attributes = params
            if (config.animStyle != 0) window.setWindowAnimations(config.animStyle)
        } else {
            // 如果是 BottomSheet，通常只需要设置背景透明，方便显示圆角
            window.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        }

        isCancelable = config.cancelOnBackPressed
        dialog?.setCanceledOnTouchOutside(config.cancelOnTouchOutside)
    }
}