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