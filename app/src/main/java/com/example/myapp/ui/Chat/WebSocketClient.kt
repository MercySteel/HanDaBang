//package com.example.myapp.ui.Chat
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import okhttp3.*
//import java.util.concurrent.Executors
//import java.util.concurrent.ScheduledFuture
//import java.util.concurrent.TimeUnit
//import java.util.concurrent.atomic.AtomicInteger
//
//object WebSocketClient {
//    private lateinit var client: OkHttpClient
//    private lateinit var webSocket: WebSocket
//    private const val MAX_RECONNECT = 5
//    private val reconnectCount = AtomicInteger(0)  // 原子计数器[5](@ref)
//    private val executor = Executors.newSingleThreadScheduledExecutor()
//    private var reconnectTask: ScheduledFuture<*>? = null
//
//    // 消息LiveData（改为普通对象避免内存泄漏）
//    object MessageLiveData : MutableLiveData<String>()
//
//    fun connect(serverIp: String) {
//        try {
//            // 先关闭旧连接
//            close()
//
//            client = OkHttpClient.Builder()
//                .pingInterval(10, TimeUnit.SECONDS)
//                .connectTimeout(15, TimeUnit.SECONDS)  // 添加连接超时[7,8](@ref)
//                .build()
//
//            val request = Request.Builder()
//                //.url("ws://10.157.78.250:8080/ws-chat")//真机用
//                .url("ws://10.0.2.2:8080/ws-chat")//模拟器用
//                .build()
//
//            webSocket = client.newWebSocket(request, createListener(serverIp))
//        } catch (e: Exception) {
//            Log.e("WebSocket", "Connection failed", e)
//            scheduleReconnect(serverIp)
//        }
//    }
//
//    private fun createListener(serverIp: String) = object : WebSocketListener() {
//        override fun onOpen(webSocket: WebSocket, response: Response) {
//            reconnectCount.set(0)  // 重置计数器
//            Log.d("WebSocket", "Connected to server")
//        }
//
//        override fun onMessage(webSocket: WebSocket, text: String) {
//            MessageLiveData.postValue(text)  // 直接传递原始消息
//        }
//
//        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//            Log.d("WebSocket", "Connection closed: $reason")
//            if (reconnectCount.get() < MAX_RECONNECT) scheduleReconnect(serverIp)
//        }
//
//        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//            Log.e("WebSocket","Connection failed: ${t.message}")
//            if (reconnectCount.get() < MAX_RECONNECT) scheduleReconnect(serverIp)
//        }
//    }
//
//    private fun scheduleReconnect(serverIp: String) {
//        reconnectTask?.cancel(false)
//        reconnectTask = executor.schedule({
//            reconnectCount.incrementAndGet()
//            connect(serverIp)
//        }, 3, TimeUnit.SECONDS)
//    }
//
//    fun sendMessage(message: String) {
//        try {
//            if (::webSocket.isInitialized && reconnectCount.get() == 0) {
//                webSocket.send(message)
//            }
//        } catch (e: Exception) {
//            Log.e("WebSocket", "Send failed", e)
//        }
//    }
//
//    fun close() {
//        reconnectTask?.cancel(true)
//        if (::webSocket.isInitialized) webSocket.close(1000, "User closed")
//        executor.shutdown()
//    }
//}