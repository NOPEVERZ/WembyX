package com.nopever.viewx.adapters

import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * 通过包名展示应用图标
 */
@BindingAdapter("bindAppIcon")
fun bindAppIcon(imageView: ImageView, packageName: String?) {
    if (packageName != null) {
        try {
            val pm = imageView.context.packageManager
            // 获取图标
            val icon = pm.getApplicationIcon(packageName)
            imageView.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            // 如果找不到包名（可能是已卸载的应用），显示默认安卓图标
            imageView.setImageResource(android.R.drawable.sym_def_app_icon)
        }
    } else {
        imageView.setImageResource(android.R.drawable.sym_def_app_icon)
    }
}

@BindingAdapter("bindIvResId")
fun bindIvResId(imageView: ImageView, resId: Int?) {
    resId?.let {
        imageView.setImageResource(resId)
    }
}