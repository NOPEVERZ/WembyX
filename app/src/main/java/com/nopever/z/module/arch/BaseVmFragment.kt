package com.nopever.z.module.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseVmFragment<VB : ViewBinding> : Fragment() {

    // 遵循 Google 官方规范：Fragment 的 Binding 必须是可空的
    private var _binding: VB? = null

    // 提供给子类使用的非空属性，仅在 onCreateView 和 onDestroyView 之间有效
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding is only valid between onCreateView and onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // 获取泛型 VB 的 Class 对象 (支持多层继承查找)
        val bindingClass = getBindingClass(javaClass)

        // 获取 inflate 方法：inflate(LayoutInflater, ViewGroup, boolean)
        val method = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType // 注意：这里必须用 boolean.class (基本类型)
        )

        // 执行 invoke 调用
        @Suppress("UNCHECKED_CAST")
        _binding = method.invoke(null, inflater, container, false) as VB

        // 如果是 DataBinding，自动绑定生命周期
        if (_binding is ViewDataBinding) {
            (_binding as ViewDataBinding).lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 置空，防止 Fragment 销毁但 View 未被回收导致的内存泄漏
        _binding = null
    }

    /**
     * 递归向上查找泛型参数，防止多层继承时(如 Base -> BaseChild -> Child) 找不到类型
     */
    @Suppress("UNCHECKED_CAST")
    private fun getBindingClass(clazz: Class<*>): Class<VB> {
        val superclass = clazz.genericSuperclass

        // 如果父类是参数化类型 (即带泛型的类)
        if (superclass is ParameterizedType) {
            // 遍历所有泛型参数
            for (arg in superclass.actualTypeArguments) {
                // 找到第一个是 ViewBinding 子类的参数
                if (arg is Class<*> && ViewBinding::class.java.isAssignableFrom(arg)) {
                    return arg as Class<VB>
                }
            }
        }

        // 如果当前层没找到，或者父类不是泛型，继续往上找
        // 比如 clazz.superclass 可能是 BaseVmFragment，那下一次循环就能找到
        val parent = clazz.superclass
        if (parent != null && parent != Any::class.java) {
            return getBindingClass(parent)
        }

        throw IllegalStateException("Could not find ViewBinding generic in class hierarchy of ${javaClass.simpleName}")
    }
}