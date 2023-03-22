package com.example.chatapp.domain.models

import com.example.chatapp.data.local.room.entity.MessageEntity

data class MessageModel(
    val chat_id: Int,
    val user: User,
    val timeSending: String = "14:23",
    val dateSending: String = "20.12.2023",
    val text: String = "Hello, world!"
)

data class Message(
    val id_message: Long,
    val id_chat: Long = 0,
    val text: String,
    val formattedTime: String,
    val username: String
) {
    fun toEntity() = MessageEntity(
        id_message = id_message,
        id_chat = id_chat,
        id_user = username,
        text = text,
        timeFormatted = formattedTime
    )
}