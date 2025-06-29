package com.example.myapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: UserDbHelper

    private var snackbar: Snackbar? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    // 计时器变量 (时分秒)
    private var elapsedHours = 0
    private var elapsedMinutes = 0
    private var elapsedSeconds = 0

    private var fabDx = 0f
    private var fabDy = 0f
    private var isLongPressMode = false
    private var longPressStartTime = 0L
    private val LONG_PRESS_DURATION = 500L // 半秒长按阈值
    private val MOVE_THRESHOLD = 10f      // 误触移动阈值(像素)
    private val fabLongPressChecker = Handler(Looper.getMainLooper())

    companion object {
        const val PREFS_NAME = "AppSettings"
        const val INTERVAL_KEY = "reminder_interval"
        const val DEFAULT_INTERVAL = 30 // 默认30分钟
    }

    // 计时器状态
    private var isTimerRunning = false

    // 图标资源
    private val playIconRes = android.R.drawable.ic_media_play // 默认播放图标
    private val stopIconRes = android.R.drawable.ic_media_pause // 运行时图标

    // 轻量级生命周期观察器
    private val appLifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onAppBackgrounded() {
            resetTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = UserDbHelper(this)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)// 获取SharedPreferences实例
        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_INDEFINITE)
//                .setAction("Action", null).setAnimationMode(ANIMATION_MODE_SLIDE)
//                .setAnchorView(R.id.fab).show()
//        }
        // 注册应用生命周期监听
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        setupFab()
        // 从保存的状态恢复（如果存在）
        savedInstanceState?.let {
            elapsedSeconds = it.getInt("elapsedSeconds", 0)
            if (it.getBoolean("timerActive", false)) {
                startTimer()
                showTimerSnackbar(binding.root)
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_chat),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupNavHeaderClick()

        loadUserInfo()
    }

    private fun setupFab() {
        // 设置初始状态为停止
        binding.appBarMain.fab.setImageResource(playIconRes)
        isTimerRunning = false

        binding.appBarMain.fab.setOnTouchListener { view, event ->
            val parentContainer = binding.appBarMain.root as ViewGroup
            val screenWidth = parentContainer.width.toFloat()
            val screenHeight = parentContainer.height.toFloat()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 重置长按状态
                    isLongPressMode = false
                    longPressStartTime = System.currentTimeMillis()

                    // 保存初始偏移
                    fabDx = view.x - event.rawX
                    fabDy = view.y - event.rawY

                    // 启动长按检测Runnable
                    fabLongPressChecker.postDelayed({
                        // 如果仍在触摸且未达到移动阈值
                        isLongPressMode = true

                        // 添加视觉反馈（可选）
                        view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start()

                        // 震动反馈（可选，已移除Vibrator引用）
                        // vibrateShort()
                    }, LONG_PRESS_DURATION)

                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val elapsed = System.currentTimeMillis() - longPressStartTime
                    val dx = abs(event.rawX - (view.x - fabDx))
                    val dy = abs(event.rawY - (view.y - fabDy))

                    // 如果在长按时间内移动距离超过阈值，取消长按检测
                    if (elapsed < LONG_PRESS_DURATION &&
                        (dx > MOVE_THRESHOLD || dy > MOVE_THRESHOLD)) {
                        fabLongPressChecker.removeCallbacksAndMessages(null)
                    }

                    if (isLongPressMode || elapsed >= LONG_PRESS_DURATION) {
                        // 计算新位置（限制在屏幕范围内）
                        var newX = event.rawX + fabDx
                        var newY = event.rawY + fabDy

                        newX = newX.coerceIn(0f, screenWidth - view.width)
                        newY = newY.coerceIn(0f, screenHeight - view.height)

                        // 实时更新位置
                        view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()
                        return@setOnTouchListener true
                    }
                    false
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 取消长按检测
                    fabLongPressChecker.removeCallbacksAndMessages(null)

                    // 重置视觉状态
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                    // 处理不同情况
                    if (isLongPressMode) {
                        // 结束长按模式
                        isLongPressMode = false
                        return@setOnTouchListener true
                    } else {
                        val elapsed = System.currentTimeMillis() - longPressStartTime
                        if (elapsed < LONG_PRESS_DURATION) {
                            // 短按处理：触发原始计时功能
                            if (!isTimerRunning) {
                                startCountdown(view)
                            } else {
                                resetTimer()
                            }
                        }
                        return@setOnTouchListener false
                    }
                }

                else -> false
            }
        }
    }


    private fun startCountdown(view: View) {
        // 重置计时器
        resetTimer()

        // 从设置中获取倒计时时间（分钟）并转换为秒
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val intervalMinutes = prefs.getInt(INTERVAL_KEY, DEFAULT_INTERVAL)

        // 计算倒计时总时间（秒）
        val totalSeconds = intervalMinutes * 60

        // 设置倒计时值
        elapsedMinutes = intervalMinutes
        elapsedSeconds = 0
        elapsedHours = 0

        // 更新状态
        isTimerRunning = true
        binding.appBarMain.fab.setImageResource(stopIconRes)

        // 显示倒计时Snackbar
        showTimerSnackbar(view)

        // 开始倒计时
        startTimer()
    }

    private fun showTimerSnackbar(view: android.view.View) {
        snackbar?.dismiss()

        snackbar = Snackbar.make(view, formatCountdownTime(), Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setAnchorView(R.id.fab)

        snackbar?.show()
    }

    // 计时器任务（现在作为倒计时器）
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (elapsedSeconds > 0 || elapsedMinutes > 0 || elapsedHours > 0) {
                // 递减倒计时
                if (elapsedSeconds > 0) {
                    elapsedSeconds--
                } else {
                    if (elapsedMinutes > 0) {
                        elapsedMinutes--
                        elapsedSeconds = 59
                    } else if (elapsedHours > 0) {
                        elapsedHours--
                        elapsedMinutes = 59
                        elapsedSeconds = 59
                    }
                }

                updateSnackbarText()

                // 检查是否倒计时结束
                if (elapsedHours == 0 && elapsedMinutes == 0 && elapsedSeconds == 0) {
                    // 倒计时结束
                    countdownFinished()
                } else {
                    // 继续倒计时
                    mainHandler.postDelayed(this, 1000)
                }
            }
        }
    }

    // 格式化倒计时时间为 MM:SS
    private fun formatCountdownTime(): String {
        // 计算总分钟数（包括小时部分）
        val totalMinutes = elapsedHours * 60 + elapsedMinutes
        return String.format("%02d:%02d", totalMinutes, elapsedSeconds)
    }

    private fun updateSnackbarText() {
        snackbar?.setText(formatCountdownTime())
    }

    private fun startTimer() {
        mainHandler.post(timerRunnable)
    }

    private fun resetTimer() {
        mainHandler.removeCallbacks(timerRunnable)
        snackbar?.dismiss()
        snackbar = null

        // 重置所有计时变量
        elapsedHours = 0
        elapsedMinutes = 0
        elapsedSeconds = 0

        // 更新UI为默认状态
        isTimerRunning = false
        binding.appBarMain.fab.setImageResource(playIconRes)
    }

    // 倒计时结束处理
    private fun countdownFinished() {
        // 移除所有回调
        mainHandler.removeCallbacks(timerRunnable)

        // 播放提示音
        playCompletionSound()

        // 更新Snackbar显示结束信息
        snackbar?.setText("倒计时结束!")
        snackbar?.setAction("重新开始") {
            startCountdown(binding.root)
        }

        // 重置按钮状态
        isTimerRunning = false
        binding.appBarMain.fab.setImageResource(playIconRes)
    }

    private fun playCompletionSound() {
        try {
            // 使用Android默认系统通知音
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            ringtone.play()
        } catch (e: Exception) {
            Log.e("Countdown", "无法播放提示音", e)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保存当前计时状态
        outState.putInt("elapsedHours", elapsedHours)
        outState.putInt("elapsedMinutes", elapsedMinutes)
        outState.putInt("elapsedSeconds", elapsedSeconds)
        outState.putBoolean("isTimerRunning", isTimerRunning)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // 恢复计时状态
        elapsedHours = savedInstanceState.getInt("elapsedHours", 0)
        elapsedMinutes = savedInstanceState.getInt("elapsedMinutes", 0)
        elapsedSeconds = savedInstanceState.getInt("elapsedSeconds", 0)
        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning", false)

        // 如果之前处于运行状态
        if (isTimerRunning) {
            // 检查是否倒计时已经结束
            if (elapsedHours == 0 && elapsedMinutes == 0 && elapsedSeconds == 0) {
                // 如果倒计时已结束，只显示结束状态
                binding.appBarMain.fab.setImageResource(playIconRes)
                showTimerSnackbar(binding.root)
                snackbar?.setText("倒计时结束!")
                snackbar?.setAction("重新开始") {
                    startCountdown(binding.root)
                }
            } else {
                // 否则继续倒计时
                binding.appBarMain.fab.setImageResource(stopIconRes)
                showTimerSnackbar(binding.root)
                startTimer()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 每次返回主界面时刷新用户信息（确保显示最新数据）
        loadUserInfo()
    }

    // 加载当前登录用户信息并更新界面
    private fun loadUserInfo() {
        // 从SharedPreferences获取当前登录用户账号
        val currentAccount = sharedPreferences.getString("current_user", null)

        if (currentAccount != null) {
            // 使用协程在后台线程加载用户数据
            lifecycleScope.launch(Dispatchers.IO) {
                val currentUser = dbHelper.getUserByAccount(currentAccount)

                withContext(Dispatchers.Main) {
                    if (currentUser != null) {
                        updateNavHeader(
                            nickname = currentUser.nickname ?: "未设置昵称",
                            motto = currentUser.motto ?: "未设置签名",
                            avatarUri = currentUser.avatarUri
                        )
                    } else {
                        Log.e("MainActivity", "用户信息获取失败: $currentAccount")
                    }
                }
            }
        } else {
            Log.e("MainActivity", "未检测到登录用户")
        }
    }

    // 更新导航头部信息
    private fun updateNavHeader(nickname: String, motto: String, avatarUri: String?) {
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.username).text = nickname
        headerView.findViewById<TextView>(R.id.motto).text = motto

        val imageView = headerView.findViewById<ImageView>(R.id.imageView)
        avatarUri?.let { uriStr ->
            Glide.with(this)
                .load(Uri.parse(uriStr))
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(imageView)
        } ?: run {
            Glide.with(this)
                .load(R.mipmap.ic_launcher_round)
                .circleCrop()
                .into(imageView)
        }
    }

    //点击头像编辑个人信息
    private fun setupNavHeaderClick() {
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderRoot = headerView.findViewById<View>(R.id.imageView)

        navHeaderRoot.setOnClickListener {
            // 直接启动UserInfoActivity，不再使用结果回调
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // 处理设置点击
                handleSettings()
                true
            }
            R.id.action_about -> {
                // 显示关于对话框
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("关于")  // 设置对话框标题
            .setMessage("\n版本: 1.0.0\n开发者: HanDaBang\n联系方式: https://github.com/MercySteel/HanDaBang") // 设置内容
            .setPositiveButton("确定") { dialog, _ -> dialog.dismiss() } // 设置确定按钮
            .create()

        // 自定义对话框样式
        dialog.setOnShowListener {
            // 设置标题文字颜色
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(ContextCompat.getColor(this, R.color.textColorSecondary))
        }

        dialog.show()
    }

    private fun handleSettings() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_settings, null)

        // 创建对话框
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // 获取视图引用
        val etInterval = dialogView.findViewById<EditText>(R.id.et_interval)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btn_save)

        // 加载当前设置的值
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentInterval = prefs.getInt(INTERVAL_KEY, DEFAULT_INTERVAL)
        etInterval.setText(currentInterval.toString())

        // 设置按钮点击事件
        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            val input = etInterval.text.toString()
            if (input.isNotEmpty()) {
                try {
                    val interval = input.toInt()
                    if (interval > 0) {
                        // 保存设置
                        prefs.edit().putInt(INTERVAL_KEY, interval).apply()

                        Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "请输入大于0的整数", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "请输入时间间隔", Toast.LENGTH_SHORT).show()
            }
        }
        // 显示对话框
        dialog.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清理资源
        resetTimer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(appLifecycleObserver)

        com.example.myapp.ui.Chat.StompClientManager.disconnect()
    }
}