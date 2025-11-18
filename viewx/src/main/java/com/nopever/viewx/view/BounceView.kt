package com.nopever.viewx.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * 一个可以实现顶部和底部回弹效果的容器布局。
 * 用法：在XML中用它包裹一个可滚动的视图（如ScrollView, RecyclerView...）。
 * v2 添加触摸空白区域也可滚动，可添加不可滚动的布局（如LinearLayout, FrameLayout, ConstraintLayout...）
 */
class BounceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 内部包裹的可滚动子视图
    private var childScrollView: View? = null

    // 用于记录上一次触摸事件的Y坐标
    private var lastY = 0f

    // 判定为滑动的最小距离
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    // 是否正在处理拖拽事件
    private var isDragging = false

    // 阻尼系数，值越大，拖动越费力
    private val dampingFactor = 2.5f

    // 初始化，在视图从XML加载完成后调用
    override fun onFinishInflate() {
        super.onFinishInflate()
        // 确保容器内有且只有一个子View
        if (childCount != 1) {
            throw IllegalStateException("BounceView can host only one direct child.")
        }
        childScrollView = getChildAt(0)
    }

    /**
     * 关键方法：事件拦截
     * 在这里决定是否要自己处理触摸事件，还是交给子View处理。
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 如果没有子View，则不拦截
        val child = childScrollView ?: return super.onInterceptTouchEvent(ev)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的初始位置
                lastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = ev.y
                val dy = currentY - lastY

                //判断是否是垂直滑动意图，只在拦截时判断是否满足拖动条件
                if (abs(dy) > touchSlop) {
                    // 检查子View是否可以垂直滚动
                    // canScrollVertically(-1) - 是否能向上滚 (内容是否在顶部)
                    // canScrollVertically(1)  - 是否能向下滚 (内容是否在底部)
                    val canScrollUp = child.canScrollVertically(-1)
                    val canScrollDown = child.canScrollVertically(1)

                    // 1. 如果子View已经到顶部，但用户还在向下滑动
                    // 2. 如果子View已经到底部，但用户还在向上滑动
                    if ((!canScrollUp && dy > 0) || (!canScrollDown && dy < 0)) {
                        // 这两种情况下，我们需要拦截事件，开始拖拽
                        isDragging = true
                        // 更新 lastY，防止在 onTouchEvent 中跳变
                        lastY = currentY
                        return true // 拦截事件
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 如果在拦截前就抬手了，确保重置状态
                isDragging = false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 关键方法：事件处理
     * 当onInterceptTouchEvent返回true后，事件会流到这里。
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 如果没有子View，则不处理
        val child = childScrollView ?: return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // *** 这是关键的修复 (Fix 1) ***
                // 当触摸事件发生在子视图之外的“空白区域”时，
                // onInterceptTouchEvent 不会拦截，事件会直接传到 onTouchEvent。
                // 我们必须在这里返回 true，来 "声明" 我们要处理这个触摸序列，
                // 这样我们才能接收到后续的 MOVE 和 UP 事件。
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val currentY = event.y
                val dy = currentY - lastY

                // *** 补充逻辑 (Fix 2) ***
                // 如果触摸是从空白区域开始的(isDragging=false)，
                // 我们需要在这里判断是否满足拖动条件。
                if (!isDragging && abs(dy) > touchSlop) {
                    val canScrollUp = child.canScrollVertically(-1)
                    val canScrollDown = child.canScrollVertically(1)

                    if ((!canScrollUp && dy > 0) || (!canScrollDown && dy < 0)) {
                        isDragging = true
                    }
                }

                if (isDragging) {
                    // 通过改变translationY来实现视图的平移
                    // 除以阻尼系数，实现越拉越费力的效果
                    child.translationY += dy / dampingFactor
                    lastY = currentY
                    return true // 消费了事件
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 手指抬起或事件取消
                if (isDragging) { // 只在拖拽时才执行动画
                    isDragging = false
                    resetAnimation()
                }
                return true // 消费了事件
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 执行回弹动画，将子View的translationY恢复到0
     */
    private fun resetAnimation() {
        childScrollView?.apply {
            animate()
                .translationY(0f)
                .setDuration(300) // 动画时长300毫秒
                .setInterpolator(DecelerateInterpolator()) // 使用减速插值器，效果更自然
                .start()
        }
    }
}