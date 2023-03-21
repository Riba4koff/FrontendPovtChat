package com.example.chatapp.data.local.room.datasources

import com.example.chatapp.data.local.room.entity.MessageEntity
import com.example.chatapp.data.local.room.messages.MessagesDao

interface IMessagesSource {
    suspend fun fetchAllMessages(): List<MessageEntity>
    suspend fun insertMessage(message: MessageEntity)
    suspend fun updateMessage(message: MessageEntity)
}

class MessagesSource(
    private val messagesDataBase: MessagesDao,
) : IMessagesSource {
    override suspend fun fetchAllMessages() = messagesDataBase.fetchAllMessages()

    override suspend fun insertMessage(message: MessageEntity) {
        messagesDataBase.insertMessage(message = message)
    }

    override suspend fun updateMessage(message: MessageEntity) {
        messagesDataBase.updateMessage(message = message)
    }
}