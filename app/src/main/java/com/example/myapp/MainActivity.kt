package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.myapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
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
    }

    //点击头像编辑个人信息
    private fun setupNavHeaderClick() {
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderRoot = headerView.findViewById<View>(R.id.imageView)

        navHeaderRoot.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)
            // 使用launcher启动而不是直接startActivity
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