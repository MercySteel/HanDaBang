package com.example.myapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //数据库结构
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

    //创建数据库
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

    //数据库更新
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // 添加用户
    fun addUser(user: User): Long {
        return writableDatabase.insert(TABLE_USERS, null,
            ContentValues().apply {
                put(COLUMN_ACCOUNT, user.account)
                put(COLUMN_PASSWORD, user.password)
                put(COLUMN_NICKNAME, user.nickname)
                put(COLUMN_MOTTO, user.motto)
                put(COLUMN_AVATAR_URI, user.avatarUri)
            }
        )
    }
    //可用put(COLUMN_PASSWORD, BCrypt.hashpw(user.password, BCrypt.gensalt()))升级为密文存储密码

    // 根据账号获取用户
    fun getUserByAccount(account: String): User? {
        readableDatabase.query(TABLE_USERS,
            arrayOf(COLUMN_ACCOUNT, COLUMN_PASSWORD, COLUMN_NICKNAME, COLUMN_MOTTO, COLUMN_AVATAR_URI),
            "$COLUMN_ACCOUNT = ?",
            arrayOf(account),
            null, null, null//分组group by，分组条件 having，排序 order by
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                return User(//结果不为空，返回用户信息
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
        readableDatabase.query(//只读数据库链接，优化性能，避免不必要的写锁定
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

    // 获取用户头像的URI
    fun getUserAvatar(account: String):String?{
        val db = readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("avatar"),
            "account = ?",
            arrayOf(account),
            null, null, null
        )

        return try {
            if (cursor.moveToFirst()) {
                cursor.getString(0)
            } else {
                null
            }
        } finally {
            cursor.close()
        }
    }

    // 更新用户头像
    fun updateUserAvatar(account: String, avatarUri: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("avatar", avatarUri)
        }
        return db.update("users", values, "account = ?", arrayOf(account)) > 0
    }
}