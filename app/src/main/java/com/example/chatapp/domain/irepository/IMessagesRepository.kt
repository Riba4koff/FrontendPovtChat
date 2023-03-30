package com.example.chatapp.domain.irepository

import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message

interface IMessagesRepository {
     suspend fun getAllMessages() : Result<List<Message>>
     suspend fun updateMessages(messages: List<Message>)
     suspend fun insertMessage(message: Message)
     suspend fun updateMessage(message: Message)
     suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse>
}