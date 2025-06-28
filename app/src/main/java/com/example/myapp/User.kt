package com.example.myapp

data class User(
    val account: String,
    val password: String,
    val nickname: String? = null,
    val motto: String? = null,
    val avatarUri: String? = null // 存储头像URI而不是Bitmap
)
