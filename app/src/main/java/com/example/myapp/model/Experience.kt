package com.example.myapp.model



import java.text.SimpleDateFormat
import java.util.*

data class Experience(
    val id: Long = 0,
    val content: String,
    val imagePath: String? = null,
    val createdAt: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
)