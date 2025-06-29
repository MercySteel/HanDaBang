package com.example.myapp

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.example.myapp.database.KaoyanDatabaseHelper
import com.example.myapp.database.extensions.insertUniversity
import com.example.myapp.model.University
import com.example.myapp.utils.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ImageLoader.init(this)
        val dbHelper = KaoyanDatabaseHelper(this)

        CoroutineScope(Dispatchers.IO).launch {
            initializeSampleData(dbHelper)
        }
    }

    private fun initializeSampleData(dbHelper: KaoyanDatabaseHelper) {
        val db = dbHelper.writableDatabase

        db.beginTransaction()
        try {
            // 检查是否已初始化
            val cursor = db.query(
                "universities",
                arrayOf("COUNT(*)"),
                null, null, null, null, null
            )
            val count = cursor.use { it.moveToFirst(); it.getInt(0) }
            if (count > 0) return

            // 清空表数据
            clearTables(db)

            // 插入数据（关键修改点）
            val tsinghua = University(
                "1", "清华大学", true, true,
                listOf(University.ScoreLine("计算机科学与技术", 2023, 350))
            )
            db.insertUniversity(tsinghua)  // 直接使用db变量

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    private fun clearTables(db: SQLiteDatabase) {
        listOf(
            "score_lines",
            "subject_details",
            "exam_subjects",
            "major_university_link",
            "majors",
            "universities",
            "experiences"
        ).forEach { table ->
            db.execSQL("DELETE FROM $table")
        }
    }
}