package com.example.myapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbHelper: UserDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化数据库帮助类
        dbHelper = UserDbHelper(this)

        // 获取SharedPreferences实例
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).setAnimationMode(ANIMATION_MODE_SLIDE)
                .setAnchorView(R.id.fab).show()
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
                            nickname = currentUser.nickname ?: currentAccount,
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
            // 直接启动UserInfo0.。Activity，不再使用结果回调
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
}