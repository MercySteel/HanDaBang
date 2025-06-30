package com.example.myapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScheduleDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Schedules.db"
        const val TABLE_SCHEDULES = "schedules"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_START_TIME = "start_time"
        const val COLUMN_END_TIME = "end_time"
        const val COLUMN_COMPLETED = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_SCHEDULES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_CONTENT TEXT,
                $COLUMN_START_TIME INTEGER NOT NULL,
                $COLUMN_END_TIME INTEGER NOT NULL,
                $COLUMN_COMPLETED INTEGER DEFAULT 0
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULES")
        onCreate(db)
    }

    // 插入新日程
    fun insertSchedule(schedule: Schedule): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, schedule.title)
            put(COLUMN_CONTENT, schedule.content)
            put(COLUMN_START_TIME, schedule.startTime)
            put(COLUMN_END_TIME, schedule.endTime)
            put(COLUMN_COMPLETED, if (schedule.isCompleted) 1 else 0)
        }
        return db.insert(TABLE_SCHEDULES, null, values).also {
            db.close() // 关闭连接避免泄漏
        }
    }
    // 更新日程
    fun updateSchedule(schedule: Schedule): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, schedule.title)
            put(COLUMN_CONTENT, schedule.content)
            put(COLUMN_START_TIME, schedule.startTime)
            put(COLUMN_END_TIME, schedule.endTime)
            put(COLUMN_COMPLETED, if (schedule.isCompleted) 1 else 0)
        }
        return db.update(
            TABLE_SCHEDULES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(schedule.id.toString())
        ).also { db.close() }
    }

    // 更新完成状态
    fun setCompletion(id: Long, completed: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COMPLETED, if (completed) 1 else 0)
        }
        return db.update(
            TABLE_SCHEDULES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        ).also { db.close() }
    }
    fun deleteSchedule(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_SCHEDULES,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        ).also { db.close() }
    }
    // 查询所有日程（按开始时间排序）
    fun getAllSchedules(): List<Schedule> {
        val schedules = mutableListOf<Schedule>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SCHEDULES,
            null, null, null,
            null, null,
            "$COLUMN_START_TIME ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                schedules.add(Schedule(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    startTime = it.getLong(it.getColumnIndexOrThrow(COLUMN_START_TIME)),
                    endTime = it.getLong(it.getColumnIndexOrThrow(COLUMN_END_TIME)),
                    isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                ))
            }
        }
        db.close()
        return schedules
    }
}