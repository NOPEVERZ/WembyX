package com.nopever.viewx.storage

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.nopever.viewx.utils.appContextX

/**
 * 一个通用的 MutableLiveData，它会自动将其值的变化持久化到 SharedPreferences。
 * 它通过 `defaultValue` 的类型来推断要使用的 SharedPreferences aPI (getString, getInt 等)。
 *
 * @param T 数据的类型 (必须是 SharedPreferences 支持的类型: String?, Int, Boolean, Float, Long)
 * @param key 用于存储的键
 * @param defaultValue 当 SharedPreferences 中没有值时返回的默认值。
 * **此值的类型决定了 SharedPreferences 将使用哪种 get/put 方法。**
 */
class SpMutableLiveData<T>(
    private val key: String,
    private val defaultValue: T
) : MutableLiveData<T>() {

    companion object {
        private val sp by lazy {
            appContextX.let {
                it.getSharedPreferences(it.packageName, Context.MODE_PRIVATE)
            }
        }
    }

    init {
        // 初始化时，从 SharedPreferences 加载值
        // 使用 super.setValue() 来设置初始值，避免触发下面的 setValue 覆盖
        super.setValue(loadFromSp())
    }

    /**
     * 当 LiveData 的值通过 setValue() 改变时被调用。
     * 我们覆盖它以将新值保存到 SharedPreferences。
     */
    override fun setValue(value: T) {
        // 只有当值真正改变时才继续
        if (super.getValue() == value) {
            return
        }
        super.setValue(value)
        spSetValue(value)
    }

    /**
     * 当 LiveData 的值通过 postValue() 改变时，
     * 它最终会在主线程上调用 setValue()。
     * 因此，我们只需要覆盖 setValue() 即可处理这两种情况。
     */
    // override fun postValue(value: T) { ... } // 不需要覆盖

    /**
     * 从 SharedPreferences 加载值。
     * 类型的判断依据是构造函数传入的 `defaultValue`。
     */
    @Suppress("UNCHECKED_CAST")
    private fun loadFromSp(): T {
        val value: Any? = when (defaultValue) {
            is String -> sp.getString(key, defaultValue)
            is Int -> sp.getInt(key, defaultValue)
            is Boolean -> sp.getBoolean(key, defaultValue)
            is Float -> sp.getFloat(key, defaultValue)
            is Long -> sp.getLong(key, defaultValue)
            null -> {
                // 如果默认值是 null，我们只能假定类型是 String?
                sp.getString(key, null)
            }
            else -> throw IllegalArgumentException("Unsupported type for SpMutableLiveData: ${defaultValue.javaClass.name}")
        }
        return value as T
    }

    /**
     * 将值保存到 SharedPreferences。
     * 类型的判断依据也是 `defaultValue`。
     */
    private fun spSetValue(value: T) {
        sp.edit {
            when (defaultValue) {
                // (value as String?) 这种写法可以安全处理 String 和 String?
                is String -> putString(key, value as String?)
                is Int -> {
                    // 对于非 String 类型，如果 T 是可空类型 (如 Int?) 且 value 为 null
                    // 我们将其重置为默认值，因为 SharedPreferences 不能存储 null 的 Int
                    if (value == null) {
                        putInt(key, defaultValue)
                        Log.w("SpMutableLiveData", "Cannot save null for Int key '$key'. Reverting to default.")
                    } else {
                        putInt(key, value as Int)
                    }
                }
                is Boolean -> {
                    if (value == null) {
                        putBoolean(key, defaultValue)
                        Log.w("SpMutableLiveData", "Cannot save null for Boolean key '$key'. Reverting to default.")
                    } else {
                        putBoolean(key, value as Boolean)
                    }
                }
                is Float -> {
                    if (value == null) {
                        putFloat(key, defaultValue)
                        Log.w("SpMutableLiveData", "Cannot save null for Float key '$key'. Reverting to default.")
                    } else {
                        putFloat(key, value as Float)
                    }
                }
                is Long -> {
                    if (value == null) {
                        putLong(key, defaultValue)
                        Log.w("SpMutableLiveData", "Cannot save null for Long key '$key'. Reverting to default.")
                    } else {
                        putLong(key, value as Long)
                    }
                }
                null -> {
                    // 默认值是 null，假定类型是 String?
                    putString(key, value as String?)
                }
                else -> throw IllegalArgumentException("Unsupported type for SpMutableLiveData: ${defaultValue.javaClass.name}")
            }
        }
    }
}