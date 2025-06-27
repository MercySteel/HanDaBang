package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private var selectedImageUri: Uri? = null

    // 图像选择启动器
    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                // 移除Glide引用，使用简单的setImageURI
                binding.avatarPreview.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        pickImage.launch(intent)
    }

    private fun saveProfile() {
        val username = binding.etUsername.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "请填写用户名", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show()
            return
        }

        // 返回结果给MainActivity
        val resultIntent = Intent().apply {
            putExtra("username", username)
            putExtra("description", description)
            putExtra("avatarUri", selectedImageUri.toString())
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}