package com.nopever.viewx.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast

/**
 * 将字符串复制到剪贴板，并根据系统版本决定是否显示 Toast。
 */
fun String.copyToClipboard(context: Context, showToast: Boolean = true, label: String = "Copied Text") {
    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, this)
        clipboard.setPrimaryClip(clip)

        if (showToast) {
            // Android 13 (API 33) 及以上系统会自动显示复制成功的悬浮窗，
            // 为了避免双重提示，我们可以只在 Android 12 及以下显示 Toast。
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// 定义一个类型别名，让代码更好读
// DateTimeTriples 等于 Pair<日期Triple, 时间Triple>
typealias DateTimeTriples = Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>

/**
 * 将 "yyyy-MM-dd HH:mm:ss" 解析为两个 Triple。
 * * @return ((年, 月, 日), (时, 分, 秒))
 * 如果解析失败，返回全 0。
 */
fun String.toDateTimeTriples(): DateTimeTriples {
    return try {
        // 1. 根据空格切分日期和时间
        // parts[0] = "2025-12-09"
        // parts[1] = "16:05:30"
        val parts = this.trim().split(" ")

        // 2. 处理日期部分 (yyyy-MM-dd)
        val dateParts = parts[0].split("-")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val day = dateParts[2].toInt()

        // 3. 处理时间部分 (HH:mm:ss)
        val timeParts = parts[1].split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val second = timeParts[2].toInt()

        // 4. 返回 Pair(Triple, Triple)
        Pair(
            Triple(year, month, day),
            Triple(hour, minute, second)
        )
    } catch (e: Exception) {
        // 出错返回全0
        Pair(Triple(0, 0, 0), Triple(0, 0, 0))
    }
}

/**
 * 解析形如 "1小时30分5秒" 的字符串，返回 Triple(小时, 分, 秒)。
 * - 如果某个单位缺失，对应的值为 0。
 * - 例如: "20分" -> (0, 20, 0)
 */
fun String.parseDurationToTriple(): Triple<Int, Int, Int> {
    // 定义一个局部函数来提取指定单位前面的数字
    fun extractValue(unit: String): Int {
        // 正则解释：
        // (\d+) : 捕获一组数字
        // $unit : 匹配具体的单位 (如 "小时")
        val regex = Regex("(\\d+)$unit")

        // 查找匹配项，如果找到，取第一个捕获组的值转为 Int，否则返回 0
        return regex.find(this)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    val hour = extractValue("小时")
    val minute = extractValue("分")
    val second = extractValue("秒")

    return Triple(hour, minute, second)
}

/**
 * 格式化时分秒。
 * 规则：值为 0 的单位不显示。
 * 例如：(1, 0, 5) -> "1小时5秒"
 * (0, 20, 0) -> "20分"
 */
fun formatHMSDuration(hour: Int, minute: Int, second: Int): String {
    val sb = StringBuilder()

    if (hour > 0) {
        sb.append(hour).append("小时")
    }

    if (minute > 0) {
        sb.append(minute).append("分")
    }

    if (second > 0) {
        sb.append(second).append("秒")
    }

    // 如果三个都是0，sb为空，根据你的需求这里返回空字符串
    // 如果你想在全0时显示 "0秒"，可以使用: if (sb.isEmpty()) return "0秒"
    return sb.toString()
}

/**
 * 将总秒数转换为 "X小时X分X秒" 格式 (扩展方法)。
 * 自动计算时分秒，并隐藏为0的单位。
 */
fun Int.toSmartDuration(): String {
    if (this <= 0) return "0秒" // 处理0或负数情况

    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    val sb = StringBuilder()
    if (hours > 0) sb.append(hours).append("小时")
    if (minutes > 0) sb.append(minutes).append("分")
    if (seconds > 0) sb.append(seconds).append("秒")

    // 假如总秒数正好是 3600 (1小时)，上面的逻辑会输出 "1小时"，seconds/minutes 没加进去，符合需求
    return sb.toString()
}

/**
 * 手机号验证
 */
fun String.isValidPhone(): Boolean {
    // 中国手机号正则：1开头，第二位为3-9，后面9位数字
    val regex = Regex("^1[3-9]\\d{9}$")
    return regex.matches(this)
}

/**
 * 隐藏身份证中间文字
 */
fun String.maskIdNo(): String {
    return when (length) {
        18 -> replaceRange(6, 14, "********")
        15 -> replaceRange(6, 12, "******")
        else -> this
    }
}

/**
 * 去掉字符串最后的.0 .00 ...
 */
fun String.dropLastDot(): String {
    return this.dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
}

/**
 * 将分钟数转换为 "X小时X分" 的格式。
 * 智能省略：
 * - 不足 60 分钟 -> 只显示 "X分"
 * - 整小时 -> 只显示 "X小时"
 */
fun Int.toDurationString(): String {
    if (this <= 0) return "0分"

    val hours = this / 60
    val minutes = this % 60

    return when {
        hours == 0 -> "${minutes}分"
        minutes == 0 -> "${hours}小时"
        else -> "${hours}小时${minutes}分"
    }
}

/**
 * string加样式
 */
fun getSpannableString(string: String, style: Int): SpannableString {
    val spannedString = SpannableString(string)
    val styleSpan = StyleSpan(style)
    spannedString.setSpan(
        styleSpan, 0, string.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE
    )
    return spannedString
}