package com.example.myapp

import java.io.Serializable
import java.util.*

data class Schedule(
    val id: Long = 0, // 新增ID字段，0表示新日程
    val title: String,
    val content: String,
    val startTime: Long,
    val endTime: Long,
    val timestamp: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }

    // 时间转换方法
    fun getStartDate() = Date(startTime)
    fun getEndDate() = Date(endTime)
}