package com.nopever.viewx.utils

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @param isBlack true-状态栏黑色文本 false-状态栏白色文本，默认黑色
 */
fun FragmentActivity.statusBarTextColor(isBlack: Boolean = true) {
    QMUIStatusBarHelper.translucent(this)
    if (isBlack)
        QMUIStatusBarHelper.setStatusBarLightMode(this)
    else
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
}

fun Fragment.statusBarTextColor(isBlack: Boolean = true) {
   this.activity?.statusBarTextColor(isBlack)
}

val Float.dp2px: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)


fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

//val Int.dp: Int
//    get() = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        this.toFloat(),
//        BaseApp.get().resources.displayMetrics
//    ).toInt()