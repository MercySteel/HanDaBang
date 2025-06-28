package com.example.myapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
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
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
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

        binding.appBarMain.fab.setOnClickListener { view ->
            if (!isTimerRunning) {
                // 开始计时
                resetTimer()
                isTimerRunning = true
                binding.appBarMain.fab.setImageResource(stopIconRes) // 切换为停止图标
                showTimerSnackbar(view)
                startTimer()
            } else {
                // 停止计时
                resetTimer()
            }
        }
    }

    private fun showTimerSnackbar(view: android.view.View) {
        snackbar?.dismiss()

        snackbar = Snackbar.make(view, formatTime(), Snackbar.LENGTH_INDEFINITE)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .setAnchorView(R.id.fab)

        snackbar?.show()
    }

    // 计时器任务
    private val timerRunnable = object : Runnable {
        override fun run() {
            elapsedSeconds++
            if (elapsedSeconds >= 60) {
                elapsedMinutes++
                elapsedSeconds = 0
            }
            if (elapsedMinutes >= 60) {
                elapsedHours++
                elapsedMinutes = 0
            }

            updateSnackbarText()
            mainHandler.postDelayed(this, 1000)
        }
    }

    // 格式化时间为 HH:MM:SS
    private fun formatTime(): String {
        return String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds)
    }

    private fun updateSnackbarText() {
        snackbar?.setText(formatTime())
    }

    private fun startTimer() {
        mainHandler.postDelayed(timerRunnable, 1000)
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
            binding.appBarMain.fab.setImageResource(stopIconRes)
            startTimer()
            showTimerSnackbar(binding.root)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清理资源
        resetTimer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(appLifecycleObserver)
    }
}