package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: UserDbHelper
    private var avatarUri: String? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                avatarUri = uri.toString()
                Glide.with(this)
                    .load(uri)
                    .into(binding.ivAvatar)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = UserDbHelper(this)

        with(binding) {
            // 设置默认头像
            Glide.with(this@RegisterActivity)
                .load(R.mipmap.ic_launcher)
                .into(ivAvatar)

            // 选择头像
            btnSelectAvatar.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                imagePickerLauncher.launch(intent)
            }

            // 注册按钮
            btnRegister.setOnClickListener {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val account = binding.etAccount.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        //val nickname = binding.etNickname.text.toString().trim().takeIf { it.isNotEmpty() }
        //val motto = binding.etMotto.text.toString().trim().takeIf { it.isNotEmpty() }

        // 使用View Binding直接访问视图
        when {
            account.isEmpty() -> binding.etAccount.error = "请输入账号"
            password.isEmpty() -> binding.etPassword.error = "请输入密码"
            password.length < 6 -> binding.etPassword.error = "密码长度至少6位"
            password != confirmPassword -> binding.etConfirmPassword.error = "两次密码不一致"
            else -> {
                if (dbHelper.accountExists(account)) {
                    binding.etAccount.error = "该账号已被注册"
                } else {
                    // 创建新用户
                    val newUser = User(
                        account = account,
                        password = password,
                        //nickname = nickname,
                        //motto = motto,
                        avatarUri = avatarUri
                    )

                    // 添加到数据库
                    if (dbHelper.addUser(newUser) != -1L) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                        finish() // 返回登录界面
                    } else {
                        Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}