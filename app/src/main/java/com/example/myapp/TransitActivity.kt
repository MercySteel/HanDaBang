package com.example.myapp


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import java.util.Random

class TransitActivity : AppCompatActivity() {

    // 图片资源ID数组（放在res/drawable中）
    private val transitionImages = arrayOf(
        R.drawable.wallpaper01,
        R.drawable.wallpaper02,
        R.drawable.wallpaper03,
        R.drawable.wallpaper04,
        R.drawable.wallpaper05,
        R.drawable.wallpaper06,
        R.drawable.wallpaper07,
        R.drawable.wallpaper08,
        R.drawable.wallpaper09,
        R.drawable.wallpaper10,
        R.drawable.wallpaper11,
        R.drawable.wallpaper12,

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transit)

        val imageView = findViewById<ImageView>(R.id.transitionImage)

        // 随机选择图片
        val randomIndex = Random().nextInt(transitionImages.size)
        imageView.setImageResource(transitionImages[randomIndex])

        // 3秒后跳转到主Activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // 结束当前Activity
        }, 2000)
    }

    // 设置全屏显示（可选）
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}