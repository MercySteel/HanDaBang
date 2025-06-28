package com.example.myapp

data class UniversityMajor(
    val schoolName: String,      // 学校名
    val majorName: String,       // 专业名
    val retestScore: Int,        // 复试分数线
    val enrollmentCount: Int,    // 招生人数
    val avgAdmissionScore: Double, // 录取平均分
    val otherInfo: String? = null // 其他信息（可为空）
) {
    // 生成主键字符串（用于唯一标识记录）
    val compositeKey: String
        get() = "$schoolName:$majorName"
}