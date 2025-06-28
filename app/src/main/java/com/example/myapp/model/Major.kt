package model

data class Major(
    val id: Int,
    val name: String,
    val description: String,
    val relatedUniversities: List<Int> // 相关院校ID列表
)