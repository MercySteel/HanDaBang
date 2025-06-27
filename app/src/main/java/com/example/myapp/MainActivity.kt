package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var dbHelper: UserDbHelper
    private lateinit var navHeader: View
    private lateinit var navHeaderImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            setOf(R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow),
            drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupNavHeaderClick()

        // 初始化数据库和共享首选项
        dbHelper = UserDbHelper(this)
        sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // 获取 NavigationView 的头部视图
        navHeader = binding.navView.getHeaderView(0)
        navHeaderImageView = navHeader.findViewById(R.id.imageView)

        // 初始化加载头像
        loadNavigationHeaderAvatar()
    }

    override fun onResume() {
        super.onResume()
        // 每次返回主界面时更新导航栏头像
        loadNavigationHeaderAvatar()
    }

    override fun onStart() {
        super.onStart()
        // 注册全局头像更新监听
        AvatarUpdateHelper.addAvatarUpdateListener(::updateNavigationHeaderAvatar)
    }

    override fun onStop() {
        super.onStop()
        // 取消注册监听
        AvatarUpdateHelper.removeAvatarUpdateListener(::updateNavigationHeaderAvatar)
    }

    // 加载导航头部的头像
    private fun loadNavigationHeaderAvatar() {
        // 1. 获取当前登录用户名
        val loggedInAccount = sharedPrefs.getString("loggedInAccount", null) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 2. 从数据库获取头像URI
                val avatarUri = dbHelper.getUserAvatar(loggedInAccount)

                // 3. 更新UI
                withContext(Dispatchers.Main) {
                    if (avatarUri != null && avatarUri.isNotBlank()) {
                        Glide.with(this@MainActivity)
                            .load(Uri.parse(avatarUri))
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round)
                            .circleCrop()
                            .into(navHeaderImageView)
                    } else {
                        navHeaderImageView.setImageResource(R.mipmap.ic_launcher_round)
                    }
                }
            } catch (e: Exception) {
                Log.e("AvatarLoad", "Error loading navigation header avatar", e)
            }
        }
    }

    // 处理头像更新通知
    private fun updateNavigationHeaderAvatar(newAvatarUri: String) {
        Glide.with(this)
            .load(Uri.parse(newAvatarUri))
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
            .circleCrop()
            .into(navHeaderImageView)
    }


    //点击头像编辑个人信息
    private fun setupNavHeaderClick() {
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderRoot = headerView.findViewById<View>(R.id.imageView)

        navHeaderRoot.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)
            editProfileLauncher.launch(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    // 接收用户信息活动返回值
    private val editProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                updateUserProfile(
                    username = it.getStringExtra("username") ?: "",
                    description = it.getStringExtra("description") ?: "",
                    avatarUri = it.getStringExtra("avatarUri")?.let { uriStr -> Uri.parse(uriStr) }
                )
            }
        }
    }

    // 更新用户信息
    private fun updateUserProfile(username: String, description: String, avatarUri: Uri?) {
        val headerView = binding.navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.username).text = username
        headerView.findViewById<TextView>(R.id.motto).text = description
        avatarUri?.let {
            headerView.findViewById<ImageView>(R.id.imageView).setImageURI(avatarUri)
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