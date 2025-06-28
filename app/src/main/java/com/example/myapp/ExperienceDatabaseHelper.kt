package com.example.myapp







import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import model.Experience
import java.util.Date
import java.util.Locale
import kotlin.apply
import kotlin.io.use

class ExperienceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "experience.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_EXPERIENCES = "experiences"
        const val COL_ID = "id"
        const val COL_CONTENT = "content"
        const val COL_IMAGE_PATH = "image_path"
        const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_EXPERIENCES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTENT TEXT NOT NULL,
                $COL_IMAGE_PATH TEXT,
                $COL_CREATED_AT TEXT
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPERIENCES")
        onCreate(db)
    }

    fun addExperience(content: String, imagePath: String?): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CONTENT, content)
            put(COL_IMAGE_PATH, imagePath)
            put(COL_CREATED_AT, System.currentTimeMillis())
        }
        return db.insert(TABLE_EXPERIENCES, null, values)
    }

    fun getAllExperiences(): List<Experience> {
        val experiences = mutableListOf<Experience>()
        val db = readableDatabase

        db.query(
            TABLE_EXPERIENCES,
            arrayOf(COL_ID, COL_CONTENT, COL_IMAGE_PATH, COL_CREATED_AT),
            null, null, null, null,
            "$COL_CREATED_AT DESC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                experiences.add(Experience(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH)),
                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        .format(Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT))))
                ))
            }
        }
        return experiences
    }
}