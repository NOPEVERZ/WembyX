package com.nopever.viewx.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.nopever.viewx.activity.CrashActivity
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
- ......................我佛慈悲....................
- ......................oo0oo.....................
- .....................o8888888o....................
- .....................88" . "88....................
- .....................(| -_- |)....................
- .....................0\  =  /0....................
- ..................._/`---'_..................
- ..................' \|     |// '.................
- ................./ \|||  :  |||// ..............
- .............../ _||||| -卍-|||||- ..............
- ..............|   | \\  -  /// |   |.............
- ..............| _|  ''---/''  |_/ |.............
- ..............\  .-__  '-'  ___/-. /.............
- ............'. .'  /--.--\  `. .'...........
- .........."" '<  `._<|>/_.' >' ""..........
- ........| | :  - \.;\ _ /;./ -  : | |.......
- ........\  \ _.   \_ __\ /__ _/   .- /  /.......
- ....=====-.____.___ _/.-`__.-'=====....
- ......................`=---='.....................
- ..................佛祖开光 ,永无BUG................
-
- @ProjectName
- @ClassName CrashUtil
- @Package com.nopever.viewx.util
- @Description 崩溃显示工具类
- @Author EDY
- @Date 2025/11/24 16:40
- @Version 1.0

 **/
@SuppressLint("StaticFieldLeak")
object CrashUtil : Thread.UncaughtExceptionHandler {


    private var mContext: Context? = null
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 初始化，在 Application 中调用
     * @param isDebug 是否是debug模式，只有debug才显示崩溃信息
     */
    fun init(context: Context, isDebug: Boolean) {
        if (!isDebug) {
            return
        }
        mContext = context
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @SuppressLint("StaticFieldLeak")
    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler?.uncaughtException(t, e)
        } else {
            // 退出程序
            try {
                Thread.sleep(1000) // 给点时间让 Toast 显示（如果有）或者 Activity 启动
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成.
     * @return true: 如果处理了该异常信息; otherwise false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null || mContext == null) {
            return false
        }

        // 收集设备参数信息
        val deviceInfo = collectDeviceInfo(mContext!!)

        //获取崩溃的堆栈信息
        val crashInfo = getCrashInfo(ex)

        // 组合完整日志
        val fullLog = StringBuilder().apply {
            append(
                "【时间】: ${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                }\n"
            )
            append(deviceInfo)
            append("\n【错误详情】:\n")
            append(crashInfo)
        }.toString()

        // 4. 跳转到 CrashActivity 显示日志
        val intent = Intent(mContext, CrashActivity::class.java)
        intent.putExtra("CRASH_DATA", fullLog)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        mContext?.startActivity(intent)

        return true
    }

    /**
     * 获取异常堆栈信息
     */
    private fun getCrashInfo(ex: Throwable): String {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        return writer.toString()
    }

    /**
     * 收集设备参数信息
     */
    private fun collectDeviceInfo(ctx: Context): String {
        val sb = StringBuilder()
        try {
            val pm = ctx.packageManager
            val pi = pm.getPackageInfo(ctx.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pi.longVersionCode.toString()
                } else {
                    pi.versionCode.toString()
                }
                sb.append("【版本信息】: VersionName=$versionName, VersionCode=$versionCode\n")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        sb.append("【设备信息】:\n")
        sb.append("  厂商: ${Build.MANUFACTURER}\n")
        sb.append("  型号: ${Build.MODEL}\n")
        sb.append("  系统: Android ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})\n")
        sb.append("  CPU: ${Build.SUPPORTED_ABIS.contentToString()}\n")

        return sb.toString()
    }
}