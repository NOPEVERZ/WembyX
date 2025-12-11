package com.nopever.viewx.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView
import java.util.regex.Pattern

/**
 * 文本高亮工具类
 */
object TextHighlightUtil {

    /**
     * 样式配置类 (Builder模式风格)
     */
    data class HighlightStyle(
        val textColor: Int? = null,              // 文字颜色
        val backgroundColor: Int? = null,        // 背景颜色
        val isBold: Boolean = false,             // 粗体
        val isItalic: Boolean = false,           // 斜体
        val isUnderline: Boolean = false,        // 下划线
        val isStrikethrough: Boolean = false,    // 删除线
        val textSizeScale: Float? = null,        // 字体缩放比例 (1.2f = 1.2倍)
        val onClick: ((String) -> Unit)? = null  // 点击回调
    )

    /**
     * 规则类：将“要匹配的内容”与“样式”绑定
     */
    data class HighlightRule(
        val pattern: Pattern,            // 匹配规则(正则)
        val style: HighlightStyle        // 对应的样式
    )

    /**
     * 核心方法：构建 SpannableString
     */
    fun buildSpannable(
        fullText: String,
        rules: List<HighlightRule>
    ): SpannableString {
        val spannable = SpannableString(fullText)

        rules.forEach { rule ->
            val matcher = rule.pattern.matcher(fullText)
            // 循环查找所有匹配项
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                applyStyle(spannable, start, end, rule.style, matcher.group())
            }
        }
        return spannable
    }

    /**
     * 应用具体的 Span 样式
     */
    private fun applyStyle(
        spannable: Spannable,
        start: Int,
        end: Int,
        style: HighlightStyle,
        matchedText: String
    ) {
        //  文字颜色
        style.textColor?.let {
            spannable.setSpan(
                ForegroundColorSpan(it),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 背景颜色
        style.backgroundColor?.let {
            spannable.setSpan(
                BackgroundColorSpan(it),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // 字体样式 (粗体/斜体)
        var styleMode = Typeface.NORMAL
        if (style.isBold) styleMode = styleMode or Typeface.BOLD
        if (style.isItalic) styleMode = styleMode or Typeface.ITALIC
        if (styleMode != Typeface.NORMAL) {
            spannable.setSpan(StyleSpan(styleMode), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // 下划线
        if (style.isUnderline) {
            spannable.setSpan(UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // 删除线
        if (style.isStrikethrough) {
            spannable.setSpan(StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        //  字体大小缩放
        style.textSizeScale?.let {
            spannable.setSpan(RelativeSizeSpan(it), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        // 点击事件
        style.onClick?.let { listener ->
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    listener(matchedText)
                }

                override fun updateDrawState(ds: TextPaint) {
                    // 默认ClickableSpan会有下划线和颜色，这里保持我们自定义的样式，去掉默认
                    // 如果style里没定义颜色，才使用link颜色
                    if (style.textColor != null) {
                        ds.color = style.textColor
                    }
                    ds.isUnderlineText = style.isUnderline // 保持自定义的下划线设置
                }
            }
            spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}

// =========================================================
// 扩展函数 (让调用更加丝滑)
// =========================================================

/**
 * 简单用法：高亮列表中的关键词
 */
fun TextView.setHighlightText(
    text: String,
    keywords: List<String>,
    color: Int = Color.RED,
    isBold: Boolean = true
) {
    if (keywords.isEmpty()) {
        this.text = text
        return
    }

    // 构建简单规则
    val style = TextHighlightUtil.HighlightStyle(textColor = color, isBold = isBold)
    val rules = keywords.filter { it.isNotEmpty() }.map { keyword ->
        // Pattern.quote 避免关键词中包含正则特殊字符导致崩溃
        TextHighlightUtil.HighlightRule(
            Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE),
            style
        )
    }

    this.text = TextHighlightUtil.buildSpannable(text, rules)
}

/**
 * 高级用法：支持 DSL 风格配置
 */
fun TextView.setRichText(fullText: String, block: HighlightBuilder.() -> Unit) {
    val builder = HighlightBuilder()
    builder.block()
    val spannable = TextHighlightUtil.buildSpannable(fullText, builder.getRules())

    this.text = spannable
    // 如果包含点击事件，必须设置 MovementMethod，否则点击无效
    if (builder.hasClickableSpan) {
        this.movementMethod = LinkMovementMethod.getInstance()
        // 移除点击时的背景色高亮（可选）
        this.highlightColor = Color.TRANSPARENT
    }
}

/**
 * DSL 构建器辅助类
 */
class HighlightBuilder {
    private val rules = mutableListOf<TextHighlightUtil.HighlightRule>()
    var hasClickableSpan = false

    // 添加普通关键词高亮
    fun addKeyword(keyword: String, style: TextHighlightUtil.HighlightStyle) {
        if (keyword.isNotEmpty()) {
            if (style.onClick != null) hasClickableSpan = true
            rules.add(
                TextHighlightUtil.HighlightRule(
                    Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE),
                    style
                )
            )
        }
    }

    // 添加正则高亮
    fun addRegex(regex: String, style: TextHighlightUtil.HighlightStyle) {
        if (regex.isNotEmpty()) {
            if (style.onClick != null) hasClickableSpan = true
            rules.add(
                TextHighlightUtil.HighlightRule(
                    Pattern.compile(regex),
                    style
                )
            )
        }
    }

    fun getRules() = rules
}

/**
 *
 * 简单使用
 *
 */
//val content = "由于天气原因，所有的航班延误，请关注最新的航班动态。"
//val keywords = listOf("航班", "延误")
//
//// 直接调用 TextView 的扩展函数
//binding.tvNotice.setHighlightText(
//text = content,
//keywords = keywords,
//color = Color.parseColor("#FF5722"), // 橙色
//isBold = true
//)

/**
 * 高阶使用
 */
//val content = "请访问官网 www.google.com 或者拨打热线 13800138000 联系客服。"
//
//binding.tvRich.setRichText(content) {
//
//    // 高亮 "客服" (红色 + 粗体 + 放大)
//    addKeyword("客服", TextHighlightUtil.HighlightStyle(
//        textColor = Color.RED,
//        isBold = true,
//        textSizeScale = 1.3f
//    ))
//
//    // 高亮 网址 (蓝色 + 下划线 + 点击事件)
//    // 正则匹配网址
//    addRegex("www\\.[a-zA-Z0-9]+\\.com", TextHighlightUtil.HighlightStyle(
//        textColor = Color.BLUE,
//        isUnderline = true,
//        onClick = { url ->
//            Toast.makeText(context, "点击了网址: $url", Toast.LENGTH_SHORT).show()
//        }
//    ))
//
//    // 高亮 手机号 (绿色 + 背景色)
//    // 正则匹配11位数字
//    addRegex("\\d{11}", TextHighlightUtil.HighlightStyle(
//        textColor = Color.WHITE,
//        backgroundColor = Color.parseColor("#4CAF50"), // 绿色背景
//        isBold = true,
//        onClick = { phone ->
//            // 这里可以写拨打电话的逻辑
//            Toast.makeText(context, "拨打: $phone", Toast.LENGTH_SHORT).show()
//        }
//    ))
//}