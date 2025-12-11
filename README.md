# WembyX-viewx

[![](https://jitpack.io/v/NOPEVERZ/WembyX.svg)](https://jitpack.io/#NOPEVERZ/WembyX)

一个简单的Android Lib，包含一些自定义view，工具类，扩展方法。  
代码都是AI生成的，包括整个库的流程，练习使用。  

出现问题，请找AI👉 &nbsp;
[Gemini](https://gemini.google.com)&emsp;
[Google AI Studio](https://aistudio.google.com)


## View
<details>
<summary><b>BounceView</b></summary>

一个轻量级的 Android 弹性滚动容器，支持顶部和底部回弹效果 (Overscroll bounce)。
支持包裹 RecyclerView, ScrollView, 甚至 LinearLayout 等任意视图。

### ✨ 特性
- **阻尼回弹**：模拟 iOS 的阻尼拖拽效果。
- **通用性强**：不仅支持可滚动控件，也支持普通布局（点击空白处也能拖拽）。
- **轻量级**：只有一个类，无多余依赖。

### 🚀 用法 (Usage)
在 XML 布局中，用 BounceView 包裹内容视图。
注意：BounceView 只能有一个直接子 View。

</details>

<details>
<summary><b>HeaderLayout</b></summary>

头布局自定义

</details>

<details>
<summary><strong>CenteredDrawableTextView</strong></summary>

一个自定义 TextView，当宽度为 match_parent 或固定宽度时，
 它会将左侧的 drawable 和文本一起居中。

</details>


## EasyDialog

<details>
<summary><strong>使用示例</strong></summary>

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

</details>

## Storage

<details>
<summary><b>SpMutableLiveData</b></summary>

一个通用的 MutableLiveData，它会自动将其值的变化持久化到 SharedPreferences。  
val test = SpMutableLiveData("key", true)  
test.setValue(false)

</details>

## Utils

<details>
<summary><b>LiveDataX</b></summary>
  
- MutableLiveData<Boolean>.toggle()或togglePost() 切换boolean

</details>

<details>
<summary><b>NumberX</b></summary>

数字相关扩展方法

</details>

<details>
<summary><b>ResourceX</b></summary>

- readAssetText(String)读取asset 文本文件     
- readRawText(String)读取 raw 文本文件

</details>

<details>
<summary><b>ScreenX</b></summary>

- FragmentActivity/Fragment.statusBarTextColor(Boolean) 设置状态栏文本颜色

</details>

<details>
<summary><b>TextHighlightUtil</b></summary>

文本高亮工具类，高亮文本，添加点击。

</details>

<details>
<summary><b>TimeX</b></summary>

时间日期相关扩展方法

</details>

<details>
<summary><b>StringX</b></summary>

字符串相关扩展方法

</details>

## CrashUtil

<details>
<summary><b>崩溃后自动跳转界面显示日志</b></summary>

```kotlin
    //App里初始化，只在debug模式显示崩溃
    override fun onCreate() {
        super.onCreate()
        CrashUtil.init(this, BuildConfig.DEBUG)
    }
```

</details>



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
    implementation 'com.github.NOPEVERZ:WembyX:0.0.12-beta'
}
```