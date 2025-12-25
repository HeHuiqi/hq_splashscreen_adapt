package com.hq.app.hq_splashscreen_adapt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var isBackDisabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreenWindowLayout()
        // 设置启动页布局
        setContentView(R.layout.activity_splash)

        // 初始化逻辑
        initializeApp()
    }

    private fun setFullScreenWindowLayout() {
        enableEdgeToEdge()
        // 刘海屏适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

    }

    private fun initImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            window.setDecorFitsSystemWindows(false)
        } else {
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }

    private fun initializeApp() {
        // 模拟初始化任务
        val initializationTasks = listOf(
            { initSdk() },
            { loadConfig() },
            { prepareData() }
        )

        // 并行执行初始化
        CoroutineScope(Dispatchers.IO).launch {
            initializationTasks.forEach { it.invoke() }

            // 所有任务完成后跳转
            withContext(Dispatchers.Main) {
                navigateToMain()

            }
        }

        // 或者：简单延迟跳转（不推荐）
        // navigateToMain()
    }

    private fun initSdk() {
        // 初始化第三方 SDK
    }

    private fun loadConfig() {
        // 加载配置
    }

    private fun prepareData() {
        // 准备初始数据
    }

    private fun navigateToMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)

            // 传递启动参数（如果需要）
            intent.putExtras(intent.extras ?: Bundle())

            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()

        }, 200)

    }


    override fun onBackPressed() {
        // 禁用返回键：直接return，不执行父类逻辑
        if (isBackDisabled) {
            return
        }
        // 如需保留默认行为（如部分场景启用），调用父类方法
        super.onBackPressed()
    }

}