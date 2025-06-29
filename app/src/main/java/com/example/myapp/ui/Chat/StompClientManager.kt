package com.example.myapp.ui.Chat

import android.util.Log
import androidx.lifecycle.MutableLiveData

import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import org.json.JSONObject


object StompClientManager {
    private lateinit var stompClient: StompClient
    val receivedMessages = MutableLiveData<String>()
    private val disposables = CompositeDisposable()

    fun connect() {
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://10.0.2.2:8080/ws-chat/websocket"
            ///websocket
        )

        // 监听生命周期事件
        val lifecycleDisposable = stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ event: LifecycleEvent ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> Log.d("STOMP", "连接已打开")
                    LifecycleEvent.Type.ERROR -> Log.e("STOMP", "连接错误", event.exception)
                    LifecycleEvent.Type.CLOSED -> Log.d("STOMP", "连接已关闭")
                    else -> {}
                }
            }, { error ->
                Log.e("STOMP", "Lifecycle 监听失败", error)
            })
        disposables.add(lifecycleDisposable)

        // 建立连接
        stompClient.connect()

        // 订阅主题
        val topicDisposable = stompClient.topic("/topic/public")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ message: StompMessage ->
                receivedMessages.postValue(message.payload)
            }, { error ->
                Log.e("STOMP", "订阅出错", error)
            })
        disposables.add(topicDisposable)
    }

    fun sendMessage(content: String, sender: String) {
        val json = JSONObject()
        json.put("content", content)
        json.put("sender", sender)
        json.put("type", "text")

        stompClient.send("/app/chat.send", json.toString())?.subscribe({
                Log.d("STOMP", "消息发送成功")
            }, {
                Log.e("STOMP", "消息发送失败", it)
            })
    }

    fun sendImage(base64: String, sender: String) {
        val json = JSONObject()
        json.put("content", base64)
        json.put("sender", sender)
        json.put("type", "image")

        stompClient.send("/app/chat.send", json.toString())
            ?.subscribe({
                Log.d("STOMP", "图片发送成功")
            }, {
                Log.e("STOMP", "图片发送失败", it)
            })
    }

    fun disconnect() {
        stompClient.disconnect()
        disposables.clear()
    }
}
