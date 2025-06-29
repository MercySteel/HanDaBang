package com.example.myapp.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.myapp.model.University

class UniversityDao(private val dbHelper: KaoyanDatabaseHelper) {

    // 表名和列名常量
    private companion object {
        const val TABLE_UNIVERSITIES = "universities"
        const val TABLE_SCORE_LINES = "score_lines"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_IS_985 = "is985"
        const val COL_IS_211 = "is211"
        const val COL_UNIVERSITY_ID = "university_id"
        const val COL_MAJOR_NAME_FK = "major_name_fk"
        const val COL_YEAR = "year"
        const val COL_SCORE = "score"
    }

    fun insertUniversity(university: University) {
        val db = dbHelper.writableDatabase.apply {
            beginTransaction()
            try {
                // 插入院校
                insert(TABLE_UNIVERSITIES, null, ContentValues().apply {
                    put(COL_ID, university.id)
                    put(COL_NAME, university.name)
                    put(COL_IS_985, university.is985)
                    put(COL_IS_211, university.is211)
                })

                // 插入分数线
                university.scoreLines.forEach { scoreLine ->
                    insert(TABLE_SCORE_LINES, null, ContentValues().apply {
                        put(COL_UNIVERSITY_ID, university.id)
                        put(COL_MAJOR_NAME_FK, scoreLine.major)
                        put(COL_YEAR, scoreLine.year)
                        put(COL_SCORE, scoreLine.score)
                    })
                }
                setTransactionSuccessful()
            } finally {
                endTransaction()
            }
        }
    }

    fun get985Universities(): List<University> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_UNIVERSITIES,
            null,
            "$COL_IS_985 = 1",
            null, null, null, null
        )

        return cursor.use {
            val result = mutableListOf<University>()
            while (it.moveToNext()) {
                result.add(University(
                    id = it.getString(it.getColumnIndexOrThrow(COL_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COL_NAME)),
                    is985 = true,
                    is211 = it.getInt(it.getColumnIndexOrThrow(COL_IS_211)) == 1,
                    scoreLines = getScoreLinesForUniversity(it.getString(it.getColumnIndexOrThrow(COL_ID)))
                ))
            }
            result
        }
    }

    private fun getScoreLinesForUniversity(universityId: String): List<University.ScoreLine> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_SCORE_LINES,
            null,
            "$COL_UNIVERSITY_ID = ?",
            arrayOf(universityId),
            null, null, null
        )

        return cursor.use {
            val result = mutableListOf<University.ScoreLine>()
            while (it.moveToNext()) {
                result.add(University.ScoreLine(
                    major = it.getString(it.getColumnIndexOrThrow(COL_MAJOR_NAME_FK)),
                    year = it.getInt(it.getColumnIndexOrThrow(COL_YEAR)),
                    score = it.getInt(it.getColumnIndexOrThrow(COL_SCORE))
                ))
            }
            result
        }
    }
}