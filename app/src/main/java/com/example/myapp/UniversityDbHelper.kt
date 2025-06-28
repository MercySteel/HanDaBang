package com.example.myapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UniversityDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // 数据库信息
        private const val DATABASE_NAME = "University.db"
        private const val DATABASE_VERSION = 2

        // 表名和列名
        private const val TABLE_ADMISSION = "admission"
        private const val COLUMN_SCHOOL = "school_name"
        private const val COLUMN_MAJOR = "major_name"
        private const val COLUMN_RETEST_SCORE = "retest_score"
        private const val COLUMN_ENROLLMENT = "enrollment_count"
        private const val COLUMN_AVG_ADMISSION_SCORE = "avg_admission_score"
        private const val COLUMN_OTHER_INFO = "other_info"

        // 创建表的SQL语句
        private const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_ADMISSION (
                $COLUMN_SCHOOL TEXT NOT NULL,
                $COLUMN_MAJOR TEXT NOT NULL,
                $COLUMN_RETEST_SCORE INTEGER NOT NULL DEFAULT 0,
                $COLUMN_ENROLLMENT INTEGER NOT NULL DEFAULT 0,
                $COLUMN_AVG_ADMISSION_SCORE REAL NOT NULL DEFAULT 0.0,
                $COLUMN_OTHER_INFO TEXT,
                PRIMARY KEY ($COLUMN_SCHOOL, $COLUMN_MAJOR)
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // 添加other_info列
            db.execSQL("ALTER TABLE $TABLE_ADMISSION ADD COLUMN $COLUMN_OTHER_INFO TEXT")
        } else {
            // 完全重建
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMISSION")
            onCreate(db)
        }
    }

    // 插入或更新专业信息（冲突时替换）
    fun addOrUpdateMajor(major: UniversityMajor): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SCHOOL, major.schoolName)
            put(COLUMN_MAJOR, major.majorName)
            put(COLUMN_RETEST_SCORE, major.retestScore)
            put(COLUMN_ENROLLMENT, major.enrollmentCount)
            put(COLUMN_AVG_ADMISSION_SCORE, major.avgAdmissionScore)
            put(COLUMN_OTHER_INFO, major.otherInfo)
        }

        return db.insertWithOnConflict(
            TABLE_ADMISSION,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    // 根据学校和专业名获取专业信息
    fun getMajor(schoolName: String, majorName: String): UniversityMajor? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ADMISSION,
            null, // 所有列
            "$COLUMN_SCHOOL = ? AND $COLUMN_MAJOR = ?",
            arrayOf(schoolName, majorName),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) cursorToMajor(it) else null
        }
    }

    // 获取所有专业
    fun getAllMajors(): List<UniversityMajor> {
        val majors = mutableListOf<UniversityMajor>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ADMISSION,
            null, // 所有列
            null, null, null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                majors.add(cursorToMajor(it))
            }
        }
        return majors
    }

    // 删除指定专业
    fun deleteMajor(schoolName: String, majorName: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_ADMISSION,
            "$COLUMN_SCHOOL = ? AND $COLUMN_MAJOR = ?",
            arrayOf(schoolName, majorName)
        )
    }

    // 更新专业信息
    fun updateMajorInfo(
        schoolName: String,
        majorName: String,
        retestScore: Int? = null,
        enrollmentCount: Int? = null,
        avgAdmissionScore: Double? = null,
        otherInfo: String? = null
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            retestScore?.let { put(COLUMN_RETEST_SCORE, it) }
            enrollmentCount?.let { put(COLUMN_ENROLLMENT, it) }
            avgAdmissionScore?.let { put(COLUMN_AVG_ADMISSION_SCORE, it) }
            otherInfo?.let { put(COLUMN_OTHER_INFO, it) } ?: putNull(COLUMN_OTHER_INFO)
        }

        return db.update(
            TABLE_ADMISSION,
            values,
            "$COLUMN_SCHOOL = ? AND $COLUMN_MAJOR = ?",
            arrayOf(schoolName, majorName)
        )
    }

    // 从Cursor转换为UniversityMajor对象
    private fun cursorToMajor(cursor: Cursor): UniversityMajor {
        return UniversityMajor(
            schoolName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SCHOOL)),
            majorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAJOR)),
            retestScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RETEST_SCORE)),
            enrollmentCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENROLLMENT)),
            avgAdmissionScore = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AVG_ADMISSION_SCORE)),
            otherInfo = cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_OTHER_INFO))
        )
    }
}

// 扩展函数：安全地从Cursor获取可为空的字符串
private fun Cursor.getStringOrNull(columnIndex: Int): String? {
    return if (isNull(columnIndex)) null else getString(columnIndex)
}