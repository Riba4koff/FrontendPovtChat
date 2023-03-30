package com.example.chatapp.data.repository

import com.example.chatapp.data.local.room.DataStores.IMessagesDataStore
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.irepository.IMessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessagesRepository(
    private val api: IAuthApi,
    private val source: IMessagesDataStore,
) : IMessagesRepository {
    override suspend fun getAllMessages(): Result<List<Message>> = withContext(Dispatchers.IO) {
        api.getAllMessages().let { result ->
            when (result) {
                is Result.Success -> {
                    Result.Success(
                        data = result.data
                    )
                }
                is Result.Error -> {
                    Result.Error(
                        data = source.fetchAllMessages().map { it.toMessage() }.reversed(),
                        message = result.message
                    )
                }
            }
        }
    }

    override suspend fun updateMessages(messages: List<Message>) {
        source.deleteAllMessages()
        messages.map { message ->
            source.insertMessage(message = message.toEntity())
        }
    }

    override suspend fun insertMessage(message: Message) {
        source.insertMessage(message = message.toEntity())
    }

    override suspend fun updateMessage(message: Message) {
        source.updateMessage(message = message.toEntity())
    }

    override suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse> =
        withContext(Dispatchers.IO) {
            try {
                source.deleteAllMessages()
                api.deleteAllMessages()
            } catch (e: Exception) {
                Result.Error(message = e.message)
            }
        }
}