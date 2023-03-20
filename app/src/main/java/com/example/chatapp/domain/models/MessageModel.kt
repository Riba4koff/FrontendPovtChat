package com.example.chatapp.domain.models

data class MessageModel(
    val chat_id: Int,
    val user: User,
    val timeSending: String = "14:23",
    val dateSending: String = "20.12.2023",
    val text: String = "Hello, world!"
)

data class Message(
    val id_chat: Int = 0,
    val text: String,
    val formattedTime: String,
    val username: String
)