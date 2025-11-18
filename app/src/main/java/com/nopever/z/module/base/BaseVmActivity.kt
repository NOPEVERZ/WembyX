package com.nopever.z.module.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.nopever.z.utils.inflate
import java.lang.reflect.ParameterizedType

abstract class BaseVmActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        ((this::class.java.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull {
            it is Class<*> && ViewDataBinding::class.java.isAssignableFrom(it)
        } as? Class<T>)?.let {
            binding = it.inflate(layoutInflater)
            setContentView(binding.root)
            binding.lifecycleOwner = this
        }
    }
}