package com.nopever.z.module.arch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint

// --- 对外暴露的超简洁扩展函数 ---

/**
 * 在 Activity 中启动一个 Fragment
 * 用法: startFragment<MyFragment>(args)
 */
inline fun <reified T : Fragment> Context.startFragment(
    args: Bundle? = null
) {
    val intent = Intent(this, FragmentAttachActivity::class.java).apply {
        putExtra(FragmentAttachActivity.EXTRA_CLASS_NAME, T::class.java.name)
        if (args != null) {
            putExtra(FragmentAttachActivity.EXTRA_ARGS, args)
        }
    }
    startActivity(intent)
}

/**
 * 在 Fragment 中启动另一个 Fragment
 */
inline fun <reified T : Fragment> Fragment.startFragment(
    args: Bundle? = null
) {
    requireContext().startFragment<T>(args)
}

// --- 容器 Activity 实现 ---
@AndroidEntryPoint
class FragmentAttachActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CLASS_NAME = "extra_class_name"
        const val EXTRA_ARGS = "extra_args"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 纯代码构建布局，无需 R.layout 文件
        val containerId = View.generateViewId()
        val container = FrameLayout(this).apply {
            id = containerId
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(container)

        if (savedInstanceState == null) {
            val className = intent.getStringExtra(EXTRA_CLASS_NAME)
            val args = intent.getBundleExtra(EXTRA_ARGS)

            if (!className.isNullOrEmpty()) {
                // 核心修改：使用 fragmentFactory.instantiate 代替 Class.forName
                // 这样能更好地处理 ClassLoader 问题，且符合 Google 规范
                val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, className)
                fragment.arguments = args

                supportFragmentManager.commit {
                    replace(containerId, fragment)
//                    setPrimaryNavigationFragment(fragment) // 可选：让 Fragment 处理返回键
                }
            }
        }
    }
}