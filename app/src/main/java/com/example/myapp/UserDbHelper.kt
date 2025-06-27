package com.example.myapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDatabase.db"
        const val TABLE_USERS = "users"
        const val COLUMN_ACCOUNT = "account"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NICKNAME = "nickname"
        const val COLUMN_MOTTO = "motto"
        const val COLUMN_AVATAR_URI = "avatar_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE_USERS (
                $COLUMN_ACCOUNT TEXT PRIMARY KEY,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_NICKNAME TEXT,
                $COLUMN_MOTTO TEXT,
                $COLUMN_AVATAR_URI TEXT
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // 添加用户
    fun addUser(user: User): Long {
        return writableDatabase.insert(
            TABLE_USERS, null,
            ContentValues().apply {
                put(COLUMN_ACCOUNT, user.account)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_NICKNAME, user.nickname)
                put(COLUMN_MOTTO, user.motto)
                put(COLUMN_AVATAR_URI, user.avatarUri)
            }
        )
    }

    // 根据账号获取用户
    fun getUserByAccount(account: String): User? {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COLUMN_ACCOUNT, COLUMN_PASSWORD, COLUMN_NICKNAME, COLUMN_MOTTO, COLUMN_AVATAR_URI),
            "$COLUMN_ACCOUNT = ?",
            arrayOf(account),
            null, null, null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                return User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            }
        }
        return null
    }

    // 验证用户凭据
    fun validateUser(account: String, password: String): Boolean {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COLUMN_ACCOUNT),
            "$COLUMN_ACCOUNT = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(account, password),
            null, null, null
        ).use { cursor ->
            return cursor.count > 0
        }
    }

    // 检查账号是否存在
    fun accountExists(account: String): Boolean {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COLUMN_ACCOUNT),
            "$COLUMN_ACCOUNT = ?",
            arrayOf(account),
            null, null, null
        ).use { cursor ->
            return cursor.count > 0
        }
    }
}