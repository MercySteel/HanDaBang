package com.example.myapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher

object ImageUtils {
    // 打开图片选择器
    fun selectImage(launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        launcher.launch(intent)
    }

    // 获取图片的URI字符串
    fun getImageUri(context: Context, uri: Uri?): String? {
        return uri?.let {
            // 实际应用中这里应该将图片复制到应用存储目录并返回该路径
            it.toString()
        }
    }
}