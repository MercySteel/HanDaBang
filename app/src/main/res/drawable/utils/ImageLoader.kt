package com.example.myapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.LruCache
import android.widget.ImageView
import com.example.myapp.R
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

object ImageLoader {
    private var placeholder: Bitmap? = null
    private val memoryCache: LruCache<String, Bitmap>

    init {
        // 获取最大可用内存
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // 使用1/8的可用内存作为缓存
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // 缓存大小以KB为单位
                return bitmap.byteCount / 1024
            }
        }
    }

    /**
     * 初始化方法，在Application或Activity中调用
     */
    fun init(context: Context) {
        placeholder = BitmapFactory.decodeResource(context.resources, R.drawable.placeholder)
    }

    /**
     * 加载图片到ImageView
     */
    fun loadImage(imageView: ImageView, imageUrl: String) {
        if (placeholder == null) {
            placeholder = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

        // 首先检查缓存
        val cachedBitmap = memoryCache.get(imageUrl)
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap)
            return
        }

        // 缓存中没有，启动任务下载图片
        BitmapDownloaderTask(imageView, placeholder, imageUrl).execute()
    }

    private class BitmapDownloaderTask(
        private val imageView: ImageView,
        private val placeholder: Bitmap?,
        private val imageUrl: String
    ) : AsyncTask<Void, Void, Bitmap?>() {
        private var imageViewReference: WeakReference<ImageView> = WeakReference(imageView)

        override fun doInBackground(vararg params: Void?): Bitmap? {
            // 首先检查缓存(虽然已经在loadImage中检查过，但这里再次检查以防并发问题)
            val cachedBitmap = memoryCache.get(imageUrl)
            if (cachedBitmap != null) {
                return cachedBitmap
            }

            // 缓存中没有，从网络下载
            return try {
                val inputStream: InputStream? = downloadUrl(imageUrl)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (isCancelled) {
                result?.recycle()
                return
            }

            val imageView = imageViewReference.get()
            if (imageView != null) {
                if (result != null) {
                    // 将下载的图片放入缓存
                    memoryCache.put(imageUrl, result)
                    imageView.setImageBitmap(result)
                } else if (placeholder != null) {
                    imageView.setImageBitmap(placeholder)
                }
            }
        }

        private fun downloadUrl(urlString: String): InputStream? {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000 /* 10秒超时 */
            conn.connectTimeout = 15000 /* 15秒连接超时 */
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.connect()
            return conn.inputStream
        }
    }
}