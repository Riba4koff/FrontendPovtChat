package com.example.chatapp.data.local.room.DataStores

import com.example.chatapp.data.local.room.entity.MessageEntity
import com.example.chatapp.data.local.room.Dao.MessagesDao
import kotlinx.coroutines.flow.Flow

interface IMessagesDataStore {
    suspend fun fetchAllMessages(): Flow<List<MessageEntity>>
    suspend fun insertMessage(message: MessageEntity)
    suspend fun updateMessage(message: MessageEntity)
    suspend fun fetchMessageById(id: Long): MessageEntity?
    suspend fun deleteAllMessages()
    suspend fun getCountMessages(): Int
}

class MessagesDataStore(
    private val messagesDataBase: MessagesDao,
) : IMessagesDataStore {
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
    override suspend fun deleteAllMessages() {
        messagesDataBase.deleteAllMessages()
    }

    override suspend fun getCountMessages(): Int = messagesDataBase.getCountMessages()
}