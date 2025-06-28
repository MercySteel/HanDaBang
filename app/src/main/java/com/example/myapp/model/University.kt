package model

data class University(
    val id: Int,
    val name: String,
    val location: String,
    val ranking: Int,
    val computerStrength: Int, // 计算机专业实力评分
    val introduction: String,
    val imageUrl: String
)