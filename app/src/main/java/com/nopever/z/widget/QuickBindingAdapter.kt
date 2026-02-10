package com.nopever.z.widget

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * QuickBindingAdapter Ultimate
 * @param layoutResId 布局id
 * @param variableId item 在 xml 中的 id (如 BR.item)
 * @param extraBindings 外部绑定的变量 Map (例如 ViewModel, Presenter, Context 等)
 * @param clickIds 需要响应点击事件的 view id 集合
 * @param longClickIds 需要响应长按事件的 view id 集合
 */
open class QuickBindingAdapter<T : Any>(
    @LayoutRes private val layoutResId: Int,
    private val variableId: Int,
    private var extraBindings: Map<Int, Any> = emptyMap(),
    private val clickIds: IntArray = intArrayOf(),
    private val longClickIds: IntArray = intArrayOf(),
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback()
) : ListAdapter<T, QuickBindingAdapter.BindingViewHolder>(diffCallback) {

    private var onBindBlock: ((binding: ViewDataBinding, item: T, position: Int) -> Unit)? = null

    // 事件回调 (View, Item, Position)
    private var onLayerClickListener: ((View, T, Int) -> Unit)? = null
    private var onLayerLongClickListener: ((View, T, Int) -> Boolean)? = null

    class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutResId, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding

        // 绑定 Item 数据
        binding.setVariable(variableId, item)

        // 绑定构造函数传入的 Map 数据
        if (extraBindings.isNotEmpty()) {
            for ((key, value) in extraBindings) {
                binding.setVariable(key, value)
            }
        }

        // 统一处理点击事件 (Root + 子 View)
        onLayerClickListener?.let { listener ->
            // Root View 点击
            holder.itemView.setOnClickListener { view ->
                listener(view, item, holder.bindingAdapterPosition)
            }
            // 子 View 点击
            clickIds.forEach { id ->
                binding.root.findViewById<View>(id)?.setOnClickListener { view ->
                    listener(view, item, holder.bindingAdapterPosition)
                }
            }
        }

        // 统一处理长按事件
        onLayerLongClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener { view ->
                listener(view, item, holder.bindingAdapterPosition)
            }
            longClickIds.forEach { id ->
                binding.root.findViewById<View>(id)?.setOnLongClickListener { view ->
                    listener(view, item, holder.bindingAdapterPosition)
                }
            }
        }

        binding.executePendingBindings()
        onBindBlock?.invoke(binding, item, holder.bindingAdapterPosition)
    }

    /**
     * 业务扩展：如果运行期间需要更新外部变量 (例如切换编辑模式/普通模式)
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateExtraBindings(newBindings: Map<Int, Any>) {
        this.extraBindings = newBindings
        notifyDataSetChanged() // 必须刷新才能应用新的变量
    }

    // --- DSL 配置 ---
    fun setOnItemClickListener(listener: (view: View, item: T, position: Int) -> Unit) {
        this.onLayerClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (view: View, item: T, position: Int) -> Boolean) {
        this.onLayerLongClickListener = listener
    }

    /**
     * 暴露给外部的 onBind DSL 配置入口
     * 处理复杂的 onBindViewHolder 逻辑
     */
    fun onBind(block: (binding: ViewDataBinding, item: T, position: Int) -> Unit) {
        this.onBindBlock = block
    }

    /**
     * 带有具体 Binding 类型的 onBind
     * B : ViewDataBinding
     */
    @Suppress("UNCHECKED_CAST")
    fun <B : ViewDataBinding> onBindTyped(block: (binding: B, item: T, position: Int) -> Unit) {
        this.onBindBlock = { binding, item, position ->
            // 这里的 try-catch 防止 XML 类型不匹配导致的崩溃，安全起见
            try {
                block(binding as B, item, position)
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }
        }
    }
}

// 默认 DiffUtil
class DefaultDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}