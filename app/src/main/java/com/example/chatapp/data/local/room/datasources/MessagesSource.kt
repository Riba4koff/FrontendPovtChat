package com.example.chatapp.data.local.room.datasources

import com.example.chatapp.data.local.room.entity.MessageEntity
import com.example.chatapp.data.local.room.messages.MessagesDao

interface IMessagesSource {
    suspend fun fetchAllMessages(): List<MessageEntity>
    suspend fun insertMessage(message: MessageEntity)
    suspend fun updateMessage(message: MessageEntity)
    suspend fun fetchMessageById(id: Long): MessageEntity?
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

    override suspend fun fetchMessageById(id: Long): MessageEntity?{
        return try {
            messagesDataBase.fetchMessageById(id)
        } catch (e: Exception) {
            null
        }
    }
}