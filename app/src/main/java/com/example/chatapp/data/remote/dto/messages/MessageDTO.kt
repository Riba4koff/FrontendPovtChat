package com.example.chatapp.data.remote.dto.messages

import com.example.chatapp.domain.Util.DateTimeUtil
import com.example.chatapp.domain.models.Message
import kotlinx.serialization.Serializable
@Serializable
data class MessageDTO(
    val id_message: Long,
    val id_chat: Long,
    val id_user: String,
    val text: String,
    val time_sending: Long,
) {
    fun toMessage() = Message(
        id_message = id_message,
        id_chat = id_chat,
        text = text,
        username = id_user,
        formattedTime = DateTimeUtil.hourAndMinute(time_sending)
    )
}
