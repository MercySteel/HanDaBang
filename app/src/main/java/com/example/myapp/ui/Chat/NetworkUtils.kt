package com.example.myapp.ui.Chat

import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

// NetworkUtils.kt
fun getLocalIpAddress(): String? {
    try {
        NetworkInterface.getNetworkInterfaces().toList().flatMap { it.inetAddresses.toList() }
            .firstOrNull { address ->
                !address.isLoopbackAddress && address is Inet4Address
            }?.let { return it.hostAddress }
    } catch (e: SocketException) {
        e.printStackTrace()
    }
    return null
}
