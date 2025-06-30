//package com.example.myapp.ui.Chat
//
//import kotlinx.coroutines.*
//import java.io.DataOutputStream
//import java.net.Socket
//
//object SocketManager {
//    private const val SERVER_IP = "10.0.2.2"
//    private const val SERVER_PORT = 8080
//    private lateinit var socket: Socket
//    private lateinit var outputStream: DataOutputStream
//
//    suspend fun connect() = withContext(Dispatchers.IO) {
//        try {
//            socket = Socket(SERVER_IP, SERVER_PORT)
//            outputStream = DataOutputStream(socket.getOutputStream())
//            println("连接服务端成功")
//        } catch (e: Exception) {
//            println("连接失败: ${e.message}")
//        }
//    }
//
//    fun sendMessage(message: String) {
//        try {
//            GlobalScope.launch(Dispatchers.IO) {
//                outputStream.writeUTF(message)
//                outputStream.flush()
//                println("已发送: $message")
//            }
//        } catch (e: Exception) {
//            println("发送失败: ${e.message}")
//        }
//    }
//}