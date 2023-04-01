package com.example.chatapp.domain.irepository

import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface IMessagesRepository {
     suspend fun getAllMessages() : Result<Flow<List<Message>>>
     suspend fun updateAllMessages(messages: List<Message>)
     suspend fun tryLoadMessagesFromDataBase(): Result<Unit>
     suspend fun insertMessageFromDataBase(message: Message)
     suspend fun updateMessageFromDataBase(message: Message)
     suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse>
}