# WembyX-viewx

[![](https://jitpack.io/v/NOPEVERZ/WembyX.svg)](https://jitpack.io/#NOPEVERZ/WembyX)

一个简单的Android View库。
大部分代码都是AI生成的，包括整个库的流程，纯纯的练习用。

## BounceView
一个轻量级的 Android 弹性滚动容器，支持顶部和底部回弹效果 (Overscroll bounce)。
支持包裹 RecyclerView, ScrollView, 甚至 LinearLayout 等任意视图。

### ✨ 特性
- **阻尼回弹**：模拟 iOS 的阻尼拖拽效果。
- **通用性强**：不仅支持可滚动控件，也支持普通布局（点击空白处也能拖拽）。
- **轻量级**：只有一个类，无多余依赖。

### 🚀 用法 (Usage)
在 XML 布局中，用 BounceView 包裹内容视图。
注意：BounceView 只能有一个直接子 View。

## HeaderLayout
头布局自定义

## EasyDialog
```kotlin
    // 在 Activity/Fragment 中直接简单调用
    showBindDialog<DialogHintLayoutBinding> { binding, dialog ->
        binding.tvTitle.text = "标题"
    
        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }
    }
    //自定义属性配置
    showBindDialog<DialogHintLayoutBinding>({
        setWidthScale(0.9f) 
        setGravity(Gravity.TOP)
        setDimAmount(0.3f)
    //            setAnim(R.style.MyDialogAnim) // 设置自定义动画
        setCanceledOnTouchOutside(false) // 禁止点击外部关闭
        setCanceledOnBackPressed(false)
        setHeightDp(300.dp) //xml父布局设置固定高度无效，可在这里设置
    }) { binding, dialog ->
        binding.tvContent.text = "内容"
        binding.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }
    }
    //底部弹窗
    showBottomBindDialog<DialogHintLayoutBinding> { binding, dialog ->
    }
    //使用EasyDialog
    EasyDialog.build(this)
        .setLayout(R.layout.dialog_hint_layout)
        .setWidthScale(0.9f)
        .setGravity(Gravity.TOP)
        .setDimAmount(0.3f)
        .setBindingListener<DialogHintLayoutBinding> { binding, dialog ->
            binding.tvTitle.text = "easy easy"
        }
        .show()
    //某个View下方锚点弹窗
    showDropDown<DialogHintLayoutBinding>(binding.tvTest) { binding, dialog ->
    }
    //锚点弹窗宽度和按钮不一样窄，自定义宽度并稍微调整位置：
    showBindDialog<DialogHintLayoutBinding> ({
        // 设置锚点，X 轴不偏移，Y 轴向下偏移 10dp
        setAnchor(binding.tvTest, yOff = 20, matchWidth = false)
        // 设置弹窗自己的宽度
        setWidth(300)
    }) { binding, dialog ->
    }
```

## CrashUtil
崩溃后自动跳转界面显示日志
```kotlin
    //App里初始化，只在debug模式显示崩溃
    override fun onCreate() {
        super.onCreate()
        CrashUtil.init(this, BuildConfig.DEBUG)
    }
```

## 📦 引入 (Installation)

**Step 1. Add the JitPack repository**
在项目根目录的 `settings.gradle` (或 `build.gradle`) 中添加仓库地址：

```gradle
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url '[https://jitpack.io](https://jitpack.io)' }
    }
}
```
**Step 2. Add the dependency 在 App 模块的 build.gradle 中添加依赖：**
```gradle
dependencies {
    implementation 'com.github.NOPEVERZ:WembyX:0.0.1-beta'
}
```