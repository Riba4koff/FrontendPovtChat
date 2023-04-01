package com.example.chatapp.domain.irepository

import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface IMessagesRepository {
     suspend fun getAllMessages() : Result<Flow<List<Message>>>
     suspend fun tryLoadMessagesFromDataStore(): Result<Unit>
     suspend fun insertMessage(message: Message)
     suspend fun updateMessage(message: Message)
     suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse>
}