package com.example.myapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapp.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var dbHelper: UserDbHelper
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImageUri: Uri? = null
    private var originalAvatarUri: String? = null

    // SharedPreferences解释：
    // 1. 用于存储少量简单数据（键值对）的轻量级存储
    // 2. 适合保存用户偏好设置、登录状态等持久化数据
    // 3. 数据在应用关闭后仍然保留，直到被清除或应用卸载
    // 4. 我们使用它来获取当前登录用户的账号

    // 图像选择启动器
    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .circleCrop() // 圆形裁剪
                    .placeholder(R.mipmap.ic_launcher) // 占位符
                    .into(binding.avatarPreview)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化数据库帮助类
        dbHelper = UserDbHelper(this)

        // 获取SharedPreferences实例
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // 从SharedPreferences获取当前登录用户的账号
        val currentAccount = sharedPreferences.getString("current_user", null)

        if (currentAccount == null) {
            Toast.makeText(this, "未检测到登录用户", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 从数据库加载当前用户的信息
        val currentUser = dbHelper.getUserByAccount(currentAccount)

        if (currentUser != null) {
            // 显示当前用户信息
            binding.etUsername.setText(currentUser.nickname ?: "")
            binding.etDescription.setText(currentUser.motto ?: "")

            // 保存原始头像URI（用于未选择新头像时保持原样）
            originalAvatarUri = currentUser.avatarUri

            // 使用Glide加载头像（如果存在）
            currentUser.avatarUri?.let { uriStr ->
                Glide.with(this)
                    .load(Uri.parse(uriStr))
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher) // 占位图
                    .error(R.mipmap.ic_launcher)      // 错误图
                    .into(binding.avatarPreview)
            } ?: run {
                // 如果没有头像，设置默认头像
                Glide.with(this)
                    .load(R.mipmap.ic_launcher)
                    .circleCrop()
                    .into(binding.avatarPreview)
            }
        }else {
            Toast.makeText(this, "用户信息获取失败", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnPickImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnSave.setOnClickListener {
            saveProfile(currentAccount)
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        pickImage.launch(intent)
    }

    private fun saveProfile(account: String) {
        val username = binding.etUsername.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "请填写用户名", Toast.LENGTH_SHORT).show()
            return
        }

        // 确定要保存的头像URI：优先使用新选择，其次保留原来的
        val avatarToSave = selectedImageUri?.toString() ?: originalAvatarUri

        // 更新数据库
        val rowsAffected = dbHelper.updateUserProfile(
            account = account,
            nickname = username,
            motto = description,
            avatarUri = avatarToSave
        )

        if (rowsAffected > 0) {
            Toast.makeText(this, "资料更新成功", Toast.LENGTH_SHORT).show()

            // 更新SharedPreferences中的显示名称
            sharedPreferences.edit().putString("display_name", username).apply()

            finish() // 关闭当前Activity
        } else {
            Toast.makeText(this, "更新失败，请重试", Toast.LENGTH_SHORT).show()
        }
    }
}