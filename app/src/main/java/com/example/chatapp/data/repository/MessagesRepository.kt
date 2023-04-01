package com.example.chatapp.data.repository

import com.example.chatapp.data.local.room.DataStores.IMessagesDataStore
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.irepository.IMessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MessagesRepository(
    private val api: IAuthApi,
    private val source: IMessagesDataStore,
) : IMessagesRepository {
    override suspend fun getAllMessages(): Result<Flow<List<Message>>> = withContext(Dispatchers.IO) {
        tryLoadMessagesFromDataStore().let { result ->
            source.fetchAllMessages().map { messages ->
                messages.map { messageEntity ->
                    messageEntity.toMessage()
                }
            }.let { flowResult ->
                when (result) {
                    is Result.Success -> {
                        Result.Success(flowResult, result.message)
                    }
                    is Result.Error -> {
                        Result.Error(flowResult, result.message)
                    }
                }
            }
        }
    }
    override suspend fun tryLoadMessagesFromDataStore(): Result<Unit> {
        return api.getAllMessages().let { result ->
            when (result) {
                is Result.Success -> {
                    result.data?.map { message ->
                        insertMessage(message = message)
                    }
                    Result.Success(message = "Подключено")
                }
                is Result.Error -> {
                    Result.Error(message = "Ошибка загрузки сообщений")
                }
            }
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