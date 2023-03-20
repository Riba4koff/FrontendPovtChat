package com.example.chatapp.domain.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class ChatModel(
    val id: Int,
    val title: String = "Название чата",
    val created: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    var timeLastMessage: String = "00:00",
    var lastMessage: String = "Последнее сообщение",
    var lastUser: User = User(),
    val messages: List<MessageModel> = emptyList(),
    val users: List<User> = emptyList()
)
