package com.nopever.viewx.utils

import androidx.annotation.RawRes

/**
 * 读取asset 文本文件
 */
fun readAssetText(fileName: String): String {
    return try {
        appContextX.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 读取 raw 文件夹下的文本文件
 * @param resId 资源ID，例如 R.raw.my_json_config
 */
fun readRawText(@RawRes resId: Int): String {
    return try {
        appContextX.resources.openRawResource(resId).bufferedReader().use {
            it.readText()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}