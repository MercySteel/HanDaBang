package com.example.myapp.model

data class Major(
    val id: Int,
    val code: String, // 新增：专业代码（如"080901"）
    val name: String,
    val description: String,
    val category: String, // 新增：专业大类（如"工学-计算机类"）
    val relatedUniversities: List<Int>,
    val typicalScore: Int? = null // 新增：典型院校分数线
) {
    // 获取带代码的完整专业名称
    fun getFullName() = "($code) $name"
}