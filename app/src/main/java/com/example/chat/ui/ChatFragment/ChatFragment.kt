package com.example.chat.ui.ChatFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: TextInputEditText
    private lateinit var adapter: MessageAdapter

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

        // 设置消息列表
        adapter = MessageAdapter()
        messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messagesRecyclerView.adapter = adapter



        // 初始化Socket连接
        lifecycleScope.launch {
            SocketManager.connect()
        }
        // 发送按钮点击事件
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                // 本地添加消息
                adapter.addMessage(Message(message, true))
                // 发送到服务器
                SocketManager.sendMessage(message)

                messageInput.text?.clear()
                messagesRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }



    }
}

data class Message(val content: String, val isSent: Boolean)

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

        fun bind(message: Message) {
            if (message.isSent) {
                sentMessage.text = message.content
                sentMessage.visibility = View.VISIBLE
                receivedMessage.visibility = View.GONE
            } else {
                receivedMessage.text = message.content
                receivedMessage.visibility = View.VISIBLE
                sentMessage.visibility = View.GONE
            }
        }
    }
}