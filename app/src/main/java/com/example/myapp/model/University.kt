package com.example.myapp.model

data class University(
    val id: String,
    val name: String,
    val is985: Boolean,
    val is211: Boolean,

    val scoreLines: List<ScoreLine>
) {
    data class ScoreLine(
        val major: String,
        val year: Int,
        val score: Int
    )
}