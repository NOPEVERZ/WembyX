package com.nopever.z.module.arch

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseVmActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 反射获取 inflate 方法
        val type = getBindingClass(javaClass)
        val method = type.getMethod("inflate", LayoutInflater::class.java)

        // 执行 inflate
        @Suppress("UNCHECKED_CAST")
        binding = method.invoke(null, layoutInflater) as VB

        // 设置布局
        setContentView(binding.root)

        // DataBinding 自动绑定生命周期
        if (binding is ViewDataBinding) {
            (binding as ViewDataBinding).lifecycleOwner = this
        }
    }

    /**
     * 递归向上查找泛型参数 (与之前 Fragment 的逻辑保持一致，增强健壮性)
     */
    @Suppress("UNCHECKED_CAST")
    private fun getBindingClass(clazz: Class<*>): Class<VB> {
        val superclass = clazz.genericSuperclass

        if (superclass is ParameterizedType) {
            for (arg in superclass.actualTypeArguments) {
                if (arg is Class<*> && ViewBinding::class.java.isAssignableFrom(arg)) {
                    return arg as Class<VB>
                }
            }
        }

        val parent = clazz.superclass
        if (parent != null && parent != Any::class.java) {
            return getBindingClass(parent)
        }

        throw IllegalStateException("Could not find ViewBinding generic in class hierarchy of ${javaClass.simpleName}")
    }
}