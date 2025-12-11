package com.nopever.viewx.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.max

/**
 * 一个自定义 TextView，当宽度为 match_parent 或固定宽度时，
 * 它会将左侧的 drawable 和文本一起居中。
 *
 * 注意：为了让这个视图正常工作，请避免设置 `android:gravity`
 * 为 `center_horizontal` 或 `center`。如果需要垂直居中，
 * 请使用 `center_vertical`。水平居中由该视图处理。
 */
class CenteredDrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        // 获取复合 drawable
        val drawables = compoundDrawables
        val leftDrawable = drawables[0] // 索引 0 是左侧 drawable

        // 仅当存在左侧 drawable 时应用自定义居中。
        if (leftDrawable != null) {
            // 获取 layout 对象用于文本测量
            val currentLayout = layout ?: return super.onDraw(canvas)

            // 计算文本的实际宽度（考虑多行）
            var textWidth = 0f
            for (i in 0 until currentLayout.lineCount) {
                textWidth = max(textWidth, currentLayout.getLineWidth(i))
            }

            // 获取 drawable 宽度和 drawable 与文本之间的间距
            val drawableWidth = leftDrawable.intrinsicWidth.toFloat()
            val drawablePadding = compoundDrawablePadding.toFloat()

            // 计算内容的总宽度（drawable + 间距 + 文本）
            val contentWidth = drawableWidth + drawablePadding + textWidth

            // 获取 TextView 的总宽度
            val viewWidth = width.toFloat()

            // 计算居中所需的起始 X 坐标
            // (视图宽度 - 内容宽度) / 2
            val startX = (viewWidth - contentWidth) / 2f

            // 计算需要平移的距离。
            // super.onDraw 会从 paddingLeft 开始绘制。我们希望从 startX 开始。
            // 所以，我们需要平移 (startX - paddingLeft)。
            val translationX = startX - paddingLeft

            // 保存当前的 canvas 状态
            canvas.save()
            // 水平平移 canvas
            canvas.translate(translationX, 0f)
            // 让 TextView 绘制其内容（现在已经被平移）
            super.onDraw(canvas)
            // 恢复 canvas 状态
            canvas.restore()

        } else {
            // 如果没有左侧 drawable，则使用默认的 TextView 绘制行为。
            // 这将像往常一样遵循 'gravity' 属性。
            super.onDraw(canvas)
        }
    }
}