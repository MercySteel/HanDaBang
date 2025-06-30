package com.example.chat.ui.ChatFragment

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.ui.Chat.StompClientManager
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
//主聊天界面，UI展示与消息交互
class ChatFragment : Fragment() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: TextInputEditText
    private lateinit var adapter: MessageAdapter
    private val PICK_IMAGE_REQUEST = 1
    private val clientId: String by lazy {
        val prefs = requireContext().getSharedPreferences("chat_prefs", Context.MODE_PRIVATE)
        var id = prefs.getString("client_id", null)
        if (id == null) {
            id = java.util.UUID.randomUUID().toString()
            prefs.edit().putString("client_id", id).apply()
        }
        id
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView)
        messageInput = view.findViewById(R.id.messageInput)
        val sendButton = view.findViewById<Button>(R.id.sendButton)

        adapter = MessageAdapter()
        messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        messagesRecyclerView.adapter = adapter

        // 初始化连接
        StompClientManager.connect()

        // 接收消息并处理
        StompClientManager.receivedMessages.observe(viewLifecycleOwner) { rawMsg ->
            try {
                val json = JSONObject(rawMsg)
                val content = json.getString("content")
                val sender = json.getString("sender")
                val type = json.optString("type", "text")
                Log.d("STOMP", "收到 sender=$sender, 本机 clientId=$clientId, type=$type")

                if (sender != clientId) {
                    val isImage = type == "image"
                    adapter.addMessage(Message(content, false, isImage = isImage))
                    scrollToLatest()
                } else {
                    Log.d("STOMP", "忽略自己发出的消息: $content")
                }

            } catch (e: Exception) {
                Log.e("ChatFragment", "JSON解析失败: $rawMsg", e)
            }
        }

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                adapter.addMessage(Message(message, true))
                StompClientManager.sendMessage(message, clientId)
                messageInput.text?.clear()
                scrollToLatest()
            }
        }

        val imageButton = view.findViewById<Button>(R.id.imageButton)
        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "选择图片"), PICK_IMAGE_REQUEST)
        }
    }

    private fun scrollToLatest() {
        messagesRecyclerView.post {
            messagesRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data ?: return
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)

            StompClientManager.sendImage(base64, clientId)
            adapter.addMessage(Message(base64, true, isImage = true))
            scrollToLatest()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        StompClientManager.disconnect()
    }
}

// 数据类

data class Message(val content: String, val isSent: Boolean, val isImage: Boolean = false)

// 适配器类

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<Message>()

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val receivedMessage: TextView = itemView.findViewById(R.id.receivedMessage)
        private val sentMessage: TextView = itemView.findViewById(R.id.sentMessage)
        private val imageViewReceived: ImageView = itemView.findViewById(R.id.imageViewReceived)
        private val imageViewSent: ImageView = itemView.findViewById(R.id.imageViewSent)

        fun bind(message: Message) {
            if (message.isImage) {
                val decodedBytes = Base64.decode(message.content, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                if (message.isSent) {
                    imageViewSent.setImageBitmap(bitmap)
                    imageViewSent.visibility = View.VISIBLE

                    sentMessage.visibility = View.GONE
                    imageViewReceived.visibility = View.GONE
                    receivedMessage.visibility = View.GONE
                } else {
                    imageViewReceived.setImageBitmap(bitmap)
                    imageViewReceived.visibility = View.VISIBLE

                    receivedMessage.visibility = View.GONE
                    imageViewSent.visibility = View.GONE
                    sentMessage.visibility = View.GONE
                }
            } else {
                if (message.isSent) {
                    sentMessage.text = message.content
                    sentMessage.visibility = View.VISIBLE

                    imageViewSent.visibility = View.GONE
                    imageViewReceived.visibility = View.GONE
                    receivedMessage.visibility = View.GONE
                } else {
                    receivedMessage.text = message.content
                    receivedMessage.visibility = View.VISIBLE

                    imageViewReceived.visibility = View.GONE
                    sentMessage.visibility = View.GONE
                    imageViewSent.visibility = View.GONE
                }
            }
        }
    }
}
