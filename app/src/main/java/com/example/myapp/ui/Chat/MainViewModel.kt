//package com.example.myapp.ui.Chat
//
////import android.net.NetworkInterface
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//
//import com.example.myapp.ui.Chat.WebSocketClient
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.net.Inet4Address
//import java.net.NetworkInterface
//
//class MainViewModel : ViewModel() {
//
//    fun connectWebSocket() {
//        viewModelScope.launch(Dispatchers.IO) {
//            getLocalIpAddress()?.let { ip ->
//                WebSocketClient.connect(ip)
//            }
//        }
//    }
//
//    fun closeWebSocket() {
//        WebSocketClient.close()
//    }
//
//    private fun getLocalIpAddress(): String? {
//        try {
//            NetworkInterface.getNetworkInterfaces().toList().flatMap { it.inetAddresses.toList() }
//                .firstOrNull { address ->
//                    !address.isLoopbackAddress && address is Inet4Address
//                }?.let { return it.hostAddress }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//}