package com.example.chatapp.domain.irepository

import com.example.chatapp.data.util.MessagesResult
import com.example.chatapp.domain.models.Message

interface IMessagesRepository {
     suspend fun getAllMessages() : MessagesResult<List<Message>>
     suspend fun insertMessage(message: Message)
     suspend fun updateMessage(message: Message)
}