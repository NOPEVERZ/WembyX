package com.nopever.z.module.reuslt

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * 这是一个不可见的 Fragment，专门用于代理 startActivityForResult
 * 负责注册、启动、接收结果、然后销毁
 *
 * 风险提示！
 * 如果 App 在后台被系统回收（Process Death）：
 * 用户打开了目标界面ResultFragment。
 * 用户切出去回微信，内存不足，系统杀掉了你的 App 进程。
 * 用户切回来，系统重建 App，重建 ResultFragment。
 * 用户选好后返回。
 * GhostResultFragment 也会被重建，但是！之前那个 callback Lambda 表达式是内存里的对象，它已经丢失了！
 * 后果：用户操作了一通，返回时，你的 Lambda 不会执行，界面没有任何反应。
 * 如果你的 App 是内部工具，或者不在意这种极端情况（概率较低），这种写法完全没问题，开发效率极高。
 */
class GhostResultFragment : Fragment() {

    // 暂存回调 Lambda
    var callback: ((ActivityResult) -> Unit)? = null

    // 待启动的 Intent
    var targetIntent: Intent? = null
    var options: Bundle? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 1. 回调结果
        callback?.invoke(result)

        // 2. 收到结果后，移除自己
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fragment 创建完成后立即启动
        targetIntent?.let {
            launcher.launch(it, androidx.core.app.ActivityOptionsCompat.makeBasic())
        }
    }
}