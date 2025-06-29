package com.example.myapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapp.model.ExamSubject
import com.example.myapp.model.Experience
import com.example.myapp.model.Major
import com.example.myapp.model.University
import java.text.SimpleDateFormat
import java.util.*

class KaoyanDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "kaoyan.db", null, 2) {  // 版本号升级到2

    companion object {
        // 原有表名和字段...
        const val TABLE_EXPERIENCES = "experiences"

        // 经验表字段
        const val COL_EXP_ID = "id"
        const val COL_CONTENT = "content"
        const val COL_IMAGE_PATH = "image_path"
        const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 原有表创建...
        createExperienceTable(db)
        db.execSQL("""
    CREATE TABLE universities (
        id TEXT PRIMARY KEY,
        name TEXT NOT NULL,
        is985 INTEGER DEFAULT 0,
        is211 INTEGER DEFAULT 0
    )
""")
    }

    private fun createExperienceTable(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_EXPERIENCES (
                $COL_EXP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTENT TEXT NOT NULL,
                $COL_IMAGE_PATH TEXT,
                $COL_CREATED_AT TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 原有表删除...
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPERIENCES")
            createExperienceTable(db)
        }
    }

    // ========== 经验表操作 ==========

    fun addExperience(content: String, imagePath: String?): Long {
        val values = ContentValues().apply {
            put(COL_CONTENT, content)
            put(COL_IMAGE_PATH, imagePath)
            put(COL_CREATED_AT, SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()))
        }
        return writableDatabase.insert(TABLE_EXPERIENCES, null, values)
    }

    fun getAllExperiences(): List<Experience> {
        val experiences = mutableListOf<Experience>()
        val cursor = readableDatabase.query(
            TABLE_EXPERIENCES,
            null, null, null, null, null, "$COL_CREATED_AT DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                experiences.add(Experience(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_EXP_ID)),
                    content = it.getString(it.getColumnIndexOrThrow(COL_CONTENT)),
                    imagePath = it.getStringOrNull(it.getColumnIndexOrThrow(COL_IMAGE_PATH)),
                    createdAt = it.getString(it.getColumnIndexOrThrow(COL_CREATED_AT))
                ))
            }
        }
        return experiences
    }

    fun deleteExperience(id: Long): Int {
        return writableDatabase.delete(
            TABLE_EXPERIENCES,
            "$COL_EXP_ID = ?",
            arrayOf(id.toString())
        )
    }

    // ========== 原有数据库操作方法保持不变 ==========

    // 辅助扩展函数
    private fun android.database.Cursor.getStringOrNull(columnIndex: Int): String? {
        return if (isNull(columnIndex)) null else getString(columnIndex)
    }
}