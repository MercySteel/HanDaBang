package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: UserDbHelper
    private var selectedImageUri: Uri? = null // 修改变量名保持一致性

    // 统一图像选择启动器
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri // 保存选中的URI对象
                Glide.with(this)
                    .load(uri)
                    .circleCrop() // 添加圆形裁剪（与UserInfoActivity一致）
                    .placeholder(R.mipmap.ic_launcher) // 占位符
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
            // 设置默认头像（与UserInfoActivity保持一致）
            Glide.with(this@RegisterActivity)
                .load(R.mipmap.ic_launcher)
                .circleCrop() // 添加圆形裁剪
                .into(ivAvatar)

            // 选择头像（使用统一的打开方法）
            btnSelectAvatar.setOnClickListener {
                openImagePicker() // 使用与UserInfoActivity相同的打开方法
            }

            // 注册按钮
            btnRegister.setOnClickListener {
                registerUser()
            }
        }
    }

    // 提取为单独方法（与UserInfoActivity一致）
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun registerUser() {
        val account = binding.etAccount.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

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
                    // 创建新用户 - 转换为字符串保存
                    val avatarString = selectedImageUri?.toString()

                    val newUser = User(
                        account = account,
                        password = password,
                        avatarUri = avatarString
                    )

                    // 添加到数据库
                    if (dbHelper.addUser(newUser) != -1L) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()

                        // 自动登录（可选）
                        getSharedPreferences("user_prefs", MODE_PRIVATE).edit()
                            .putString("current_user", account)
                            .apply()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // 关闭注册界面
                    } else {
                        Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}