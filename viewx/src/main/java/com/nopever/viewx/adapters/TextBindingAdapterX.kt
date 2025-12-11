package com.nopever.viewx.adapters

import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * 文本高亮
 */
@BindingAdapter(value = ["highlightText", "highlightColor"], requireAll = true)
fun textHighlight(textView: TextView, highLight: String, color: String) {
    val text = textView.text.toString()
    val spannable = SpannableStringBuilder(text)
    var startIndex = 0
    val targetLength = highLight.length
    while (startIndex <= text.length - targetLength) {
        val index = text.indexOf(highLight, startIndex)
        if (index == -1) break
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor(color)),
            index,
            index + targetLength,
            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        startIndex = index + targetLength
    }
    textView.text = spannable
}

@BindingAdapter("bindPaintFlags")
fun bindPaintFlags(textView: TextView, flags: Int) {
    textView.apply {
        paint.flags = flags
    }
}

/**
 * true-下划线 false-删除线
 */
@BindingAdapter("lineText")
fun lineText(textView: TextView, underline: Boolean) {
    textView.apply {
        paint.flags = Paint.ANTI_ALIAS_FLAG or (if (underline) Paint.UNDERLINE_TEXT_FLAG else Paint.STRIKE_THRU_TEXT_FLAG)
    }
}

//@BindingAdapter("lineText")
//fun lineText(textView: TextView, underline: Boolean) {
//    val paint = textView.paint
//    if (underline) {
//        // 添加下划线，移除删除线
//        paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
//        paint.flags = paint.flags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//    } else {
//        // 添加删除线，移除下划线
//        paint.flags = paint.flags or Paint.STRIKE_THRU_TEXT_FLAG
//        paint.flags = paint.flags and Paint.UNDERLINE_TEXT_FLAG.inv()
//    }
//    // 确保重绘
//    textView.invalidate()
//}