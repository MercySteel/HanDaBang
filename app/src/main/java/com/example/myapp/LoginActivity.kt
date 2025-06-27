package com.example.myapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 初始化视图组件
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        // 设置登录按钮点击事件
        btnLogin.setOnClickListener {
            performLogin()
        }

        // 设置注册文本点击事件
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // 简单的输入验证
        when {
            username.isEmpty() -> etUsername.error = "请输入用户名"
            password.isEmpty() -> etPassword.error = "请输入密码"
            password.length < 6 -> etPassword.error = "密码长度至少6位"
            else -> {
                // 实际应用中这里应该是网络请求验证
                if (validateCredentials(username, password)) {
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
                    // 实际应用中这里会进入主界面
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateCredentials(username: String, password: String): Boolean {
        // 这里应该是验证逻辑，简化演示使用固定账号
        return username == "admin" && password == "123456"
    }
}