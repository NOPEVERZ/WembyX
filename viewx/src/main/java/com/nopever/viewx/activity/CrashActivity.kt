package com.nopever.viewx.activity


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess
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
- @ClassName CrashActivity
- @Package com.nopever.viewx.activity
- @Description   专门用于显示崩溃日志的 Activity
- @Author EDY
- @Date 2025/11/24 16:46
- @Version 1.0

 **/

class CrashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val crashLog = intent.getStringExtra("CRASH_DATA") ?: "未获取到崩溃日志"

        val scrollView = ScrollView(this).apply {
            setBackgroundColor(Color.WHITE)
            isFillViewport = true
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 50, 30, 30)
        }

        val titleTv = TextView(this).apply {
            text = "哎呀，APP 崩溃了！"
            textSize = 20f
            setTextColor(Color.RED)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val btnLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 20, 0, 20)
        }

        val btnRestart = Button(this).apply {
            text = "重启APP"
            setOnClickListener {
                // 重启应用的逻辑（简单版）
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(launchIntent)
                }
                finish()
                exitProcess(0)
            }
        }

        val btnCopy = Button(this).apply {
            text = "复制日志"
            setOnClickListener {
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val mClipData = ClipData.newPlainText("CrashLog", crashLog)
                cm.setPrimaryClip(mClipData)
                Toast.makeText(this@CrashActivity, "日志已复制", Toast.LENGTH_SHORT).show()
            }
        }

        btnLayout.addView(btnRestart)
        btnLayout.addView(btnCopy)

        val logTv = TextView(this).apply {
            text = crashLog
            textSize = 12f
            setTextColor(Color.BLACK)
            // 允许文字选择
            setTextIsSelectable(true)
        }

        container.addView(titleTv)
        container.addView(btnLayout)
        container.addView(logTv)
        scrollView.addView(container)

        setContentView(scrollView)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        // super.onBackPressed()
//        Toast.makeText(this, "请点击重启APP", Toast.LENGTH_SHORT).show()
    }
}