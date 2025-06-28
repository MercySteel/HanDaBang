package com.example.myapp.model

data class ExamSubject(
    val id: Int,
    val universityId: Int,
    val majorId: Int,
    val subjects: List<String>, // 考试科目列表
    val referenceBooks: List<String> // 参考书目
)