package com.nopever.viewx.data

import android.os.Parcelable
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.StyleRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class DialogConfig(
    var layoutId: Int = 0,
    var bindingClassName: String? = null, // 存储 Binding 的全类名
    var width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var gravity: Int = Gravity.CENTER,
    var dimAmount: Float = 0.5f,
    var alpha: Float = 1.0f,
    var widthScale: Float = 0f,  // 0f 表示不使用比例
    var heightScale: Float = 0f,
    var radius: Float = 0f,
    var cancelOnTouchOutside: Boolean = true,
    var cancelOnBackPressed: Boolean = true,  // 控制返回键
    @StyleRes var animStyle: Int = 0,
    var isBottomSheet: Boolean = false, // 标记是否为底部弹窗

    // 【新增】绝对坐标控制
    var isAbsolutePos: Boolean = false, // 是否启用绝对坐标模式
    var dialogX: Int = 0,
    var dialogY: Int = 0
) : Parcelable
