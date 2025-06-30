package com.example.myapp.model


data class ExamSubject(
    val id: String,
    val universityId: String, // 关联院校ID
    val universityName: String,
    val majorId: String,     // 关联专业ID
    val majorName: String,
    val subjects: List<SubjectDetail> // 考试科目详情
) {
    data class SubjectDetail(
        val code: String,    // 科目代码（如"101"）
        val name: String,    // 科目名称（如"政治"）
        val isRequired: Boolean, // 是否必考
        val examType: String // 考试类型（统考/自命题）
    )
}