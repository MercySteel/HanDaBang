package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: UserDbHelper
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDbHelper(this)
        sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // 检查是否已登录
        if (sharedPrefs.getString("loggedInAccount", null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        with(binding) {
            // 登录按钮
            btnLogin.setOnClickListener {
                performLogin()
            }

            // 注册选项
            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun performLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        when {
            username.isEmpty() -> binding.etUsername.error = "请输入账号"
            password.isEmpty() -> binding.etPassword.error = "请输入密码"
            else -> {
                if (dbHelper.validateUser(username, password)) {
                    // 保存登录状态
                    sharedPrefs.edit().putString("loggedInAccount", username).apply()

                    // 跳转到主界面
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    binding.etPassword.error = "账号或密码错误"
                }
            }
        }
    }
}