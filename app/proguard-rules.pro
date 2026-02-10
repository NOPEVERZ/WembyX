# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.nopever.z.data.model.bean.** { *; }

#即使使用了 instantiate，凡是通过类名（String）反射创建的 Fragment，都必须在 ProGuard/R8 规则中 keep 住类名?
-keep public class * extends androidx.fragment.app.Fragment { *; }

# 保持 ViewBinding 的 inflate 方法不被混淆
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
    public static ** inflate(...);
}