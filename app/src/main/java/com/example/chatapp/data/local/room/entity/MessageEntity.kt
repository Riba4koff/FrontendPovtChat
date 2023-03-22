package com.example.chatapp.data.local.room.entity

import androidx.room.*
import com.example.chatapp.domain.models.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    val id_message: Long ?= 0,
    val id_chat: Long,
    val id_user: String,
    val text: String,
    val timeFormatted: String
) {
    fun toMessage() = Message(
        id_message = id_message ?: 0,
        id_chat = id_chat,
        username = id_user,
        text = text,
        formattedTime = timeFormatted
    )
}
