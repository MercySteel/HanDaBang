package com.example.myapp.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getIntOrNull
import com.example.myapp.model.Major

class MajorDao(private val dbHelper: KaoyanDatabaseHelper) {

    // 表名和列名常量
    private companion object {
        const val TABLE_MAJORS = "majors"
        const val TABLE_MAJOR_UNIVERSITY_LINK = "major_university_link"
        const val COL_ID = "id"
        const val COL_CODE = "code"
        const val COL_NAME = "name"
        const val COL_DESCRIPTION = "description"
        const val COL_CATEGORY = "category"
        const val COL_TYPICAL_SCORE = "typical_score"
        const val COL_UNIVERSITY_ID = "university_id"
        const val COL_MAJOR_ID = "major_id"
    }

    fun insertMajor(major: Major) {
        val db = dbHelper.writableDatabase.apply {
            beginTransaction()
            try {
                // 插入专业
                insert(TABLE_MAJORS, null, ContentValues().apply {
                    put(COL_ID, major.id)
                    put(COL_CODE, major.code)
                    put(COL_NAME, major.name)
                    put(COL_DESCRIPTION, major.description)
                    put(COL_CATEGORY, major.category)
                    put(COL_TYPICAL_SCORE, major.typicalScore)
                })

                // 插入关联院校
                major.relatedUniversities.forEach { uniId ->
                    insert(TABLE_MAJOR_UNIVERSITY_LINK, null, ContentValues().apply {
                        put(COL_MAJOR_ID, major.id)
                        put(COL_UNIVERSITY_ID, uniId)
                    })
                }
                setTransactionSuccessful()
            } finally {
                endTransaction()
            }
        }
    }

    fun getMajorsWithUniversities(): List<Major> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_MAJORS,
            null, null, null, null, null, null
        )

        return cursor.use {
            val result = mutableListOf<Major>()
            while (it.moveToNext()) {
                result.add(Major(
                    id = it.getInt(it.getColumnIndexOrThrow(COL_ID)),
                    code = it.getString(it.getColumnIndexOrThrow(COL_CODE)),
                    name = it.getString(it.getColumnIndexOrThrow(COL_NAME)),
                    description = it.getString(it.getColumnIndexOrThrow(COL_DESCRIPTION)),
                    category = it.getString(it.getColumnIndexOrThrow(COL_CATEGORY)),
                    relatedUniversities = getRelatedUniversities(it.getInt(it.getColumnIndexOrThrow(COL_ID))),
                    typicalScore = it.getIntOrNull(it.getColumnIndexOrThrow(COL_TYPICAL_SCORE))
                ))
            }
            result
        }
    }

    private fun getRelatedUniversities(majorId: Int): List<Int> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_MAJOR_UNIVERSITY_LINK,
            arrayOf(COL_UNIVERSITY_ID),
            "$COL_MAJOR_ID = ?",
            arrayOf(majorId.toString()),
            null, null, null
        )

        return cursor.use {
            val result = mutableListOf<Int>()
            while (it.moveToNext()) {
                result.add(it.getString(0).toInt())
            }
            result
        }
    }
}