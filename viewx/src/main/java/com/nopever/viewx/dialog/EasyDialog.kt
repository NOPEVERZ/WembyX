package com.nopever.viewx.dialog

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.nopever.viewx.data.DialogConfig

class EasyDialog private constructor(private val fragmentManager: FragmentManager) {

    private val config = DialogConfig()
    // 两种回调二选一
    private var onBindView: ((DialogViewHolder, UniversalDialogFragment) -> Unit)? = null
    private var onBindingInflated: ((ViewBinding, UniversalDialogFragment) -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null
    private var tag: String = "EasyDialog"

    companion object {
        fun build(activity: FragmentActivity) = EasyDialog(activity.supportFragmentManager)
        fun build(fragment: Fragment) = EasyDialog(fragment.childFragmentManager)
        fun build(fragmentManager: FragmentManager) = EasyDialog(fragmentManager)
    }

    fun setLayout(@LayoutRes layoutId: Int) = apply { config.layoutId = layoutId }

    fun setWidth(width: Int) = apply { config.width = width }

    fun setHeight(height: Int) = apply { config.height = height }

    fun setWidthScale(@FloatRange(from = 0.0, to = 1.0) scale: Float) = apply { config.widthScale = scale }

    fun setHeightScale(@FloatRange(from = 0.0, to = 1.0) scale: Float) = apply { config.heightScale = scale }

    fun setGravity(gravity: Int) = apply { config.gravity = gravity }

    fun setDimAmount(dim: Float) = apply { config.dimAmount = dim }

    fun setAlpha(alpha: Float) = apply { config.alpha = alpha }

    fun setAnim(@StyleRes anim: Int) = apply { config.animStyle = anim }

    /**
     * 设置固定高度 (dp)
     */
    fun setHeightDp(dp: Int) = apply {
        val px = (dp * android.content.res.Resources.getSystem().displayMetrics.density).toInt()
        config.height = px
    }

    /**
     * 设置点击系统返回键是否取消
     * true: 点击返回键关闭弹窗 (默认)
     * false: 点击返回键无效 (拦截返回事件)
     */
    fun setCanceledOnBackPressed(enable: Boolean) = apply {
        config.cancelOnBackPressed = enable
    }

    fun setCancelable(enable: Boolean) = apply { config.cancelOnBackPressed = enable }

    fun setCanceledOnTouchOutside(enable: Boolean) = apply { config.cancelOnTouchOutside = enable }

    // 标记为底部弹窗模式
    fun asBottomSheet() = apply { config.isBottomSheet = true }

    fun onDismiss(action: () -> Unit) = apply { this.onDismiss = action }

    // 【新增】设置 Binding 类型，并保存类名到 Config
    fun <T : ViewBinding> setBinding(clazz: Class<T>) = apply {
        config.bindingClassName = clazz.name
    }

    /**
     * 设置锚点 View (实现 PopupWindow 效果)
     * 默认显示在 View 的正下方
     *
     * @param anchor 目标 View
     * @param xOff X轴偏移量
     * @param yOff Y轴偏移量
     * @param gravity相较于 anchor 的位置，目前主要支持 Gravity.BOTTOM (下方)
     * @param matchWidth 是否将弹窗宽度设置为与 anchor View 一致
     * @param horizontalGravity 水平对齐方式:Gravity.START (默认，左对齐) Gravity.END (右对齐)
     */
    fun setAnchor(
        anchor: View,
        xOff: Int = 0,
        yOff: Int = 0,
        matchWidth: Boolean = false,
        horizontalGravity: Int = Gravity.START // 【新增参数】
    ) = apply {
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        val anchorX = location[0]
        val anchorY = location[1]

        config.isAbsolutePos = true
        config.dimAmount = 0f

        // 计算 Y 轴 (始终是在 View 下方)
        config.dialogY = anchorY + anchor.height + yOff

        if (matchWidth) {
            config.width = anchor.width
            config.widthScale = 0f
            // 宽度一致时，左右对齐无所谓，使用左对齐即可
            config.gravity = Gravity.TOP or Gravity.START
            config.dialogX = anchorX + xOff
        } else {
            // 【核心逻辑】根据对齐方式计算 X
            if ((horizontalGravity and Gravity.END) == Gravity.END ||
                (horizontalGravity and Gravity.RIGHT) == Gravity.RIGHT) {

                // === 右对齐模式 ===
                // 设置重心为：右上角
                config.gravity = Gravity.TOP or Gravity.END

                // 获取屏幕宽度
                val screenWidth = Resources.getSystem().displayMetrics.widthPixels
                // 计算控件右边缘的 X 坐标
                val anchorRightX = anchorX + anchor.width

                // 计算 x 值：即 "屏幕右边缘" 到 "控件右边缘" 的距离
                // 注意：在 Gravity.END 下，x 是距离右边的偏移量
                // xOff: 正数表示向右偏移(视觉上)，负数向左。
                // 因为是从右边往左算，为了符合直觉(xOff正数向右移)，这里应该是 减去 xOff
                config.dialogX = (screenWidth - anchorRightX) - xOff

            } else {
                // === 左对齐模式 (默认) ===
                config.gravity = Gravity.TOP or Gravity.START
                config.dialogX = anchorX + xOff
            }
        }
    }

    /**
     * 普通 View 回调
     */
    fun setConvertListener(listener: (holder: DialogViewHolder, dialog: UniversalDialogFragment) -> Unit) = apply {
        this.onBindView = listener
    }

    /**
     * DataBinding 回调 (高级用法)
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ViewBinding> setBindingListener(listener: (binding: T, dialog: UniversalDialogFragment) -> Unit) = apply {
        this.onBindingInflated = { binding, dialog ->
            listener(binding as T, dialog)
        }
    }

    fun show(tag: String? = null) {
        val finalTag = tag ?: System.currentTimeMillis().toString()
        val fragment = UniversalDialogFragment.newInstance(config)
        // 注入回调
        fragment.onBindView = this.onBindView
        fragment.onBindingInflated = this.onBindingInflated // 注入 Binding 回调
        fragment.onDismissAction = this.onDismiss

        try {
            fragment.show(fragmentManager, finalTag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}