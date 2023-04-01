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
    override suspend fun getAllMessages(): Result<Flow<List<Message>>> =
        withContext(Dispatchers.IO) {
            tryLoadMessagesFromDataBase().let { result ->
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

    override suspend fun tryLoadMessagesFromDataBase(): Result<Unit> {
        return api.getAllMessages().let { result ->
            when (result) {
                is Result.Success -> {
                    source.getCountMessages().let { tableSize ->
                        if (tableSize != result.data?.size) {
                            updateAllMessages(result.data ?: emptyList())
                        }
                    }
                    result.data?.map { message ->
                        insertMessageFromDataBase(message = message)
                    }
                    Result.Success(message = "Подключено")
                }
                is Result.Error -> {
                    Result.Error(message = "Ошибка загрузки сообщений")
                }
            }
        }
    }
    override suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse> =
        withContext(Dispatchers.IO) {
            try {
                api.deleteAllMessages().let { response ->
                    if (response.data!!.successful) {
                        source.deleteAllMessages()
                    }
                    response
                }
            } catch (e: Exception) {
                Result.Error(message = e.message)
            }
        }
    override suspend fun updateAllMessages(messages: List<Message>) {
        source.deleteAllMessages()
        messages.map { message ->
            insertMessageFromDataBase(message = message)
        }
    }
    override suspend fun insertMessageFromDataBase(message: Message) {
        source.insertMessage(message = message.toEntity())
    }
    override suspend fun updateMessageFromDataBase(message: Message) {
        source.updateMessage(message = message.toEntity())
    }
}