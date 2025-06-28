package com.example.myapp

import android.app.Application
import com.example.myapp.utils.ImageLoader

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化ImageLoader
        ImageLoader.init(this)

    }
}