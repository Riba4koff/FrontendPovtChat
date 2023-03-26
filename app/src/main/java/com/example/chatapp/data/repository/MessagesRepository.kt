package com.example.chatapp.data.repository

import com.example.chatapp.data.local.room.datasources.IMessagesSource
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.util.MessagesResult
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.irepository.IMessagesRepository
import io.ktor.network.sockets.*
import retrofit2.HttpException
import java.net.ConnectException

class MessagesRepository(
    private val api: IAuthApi,
    private val source: IMessagesSource,
) : IMessagesRepository {
    override suspend fun getAllMessages(): MessagesResult<List<Message>> {
        return api.getAllMessages().let { result ->
            when (result) {
                is MessagesResult.Success -> {
                    result.data?.let { messages ->
                        updateMessages(messages = messages)
                    }
                    MessagesResult.Success(
                        data = source.fetchAllMessages().reversed().map { it.toMessage() }
                    )
                }
                is MessagesResult.Error -> {
                    MessagesResult.Error(
                        data = source.fetchAllMessages().reversed().map { it.toMessage() })
                }
            }
        }
    }

    override suspend fun updateMessages(messages: List<Message>) {
        messages.map { message ->
            source.fetchMessageById(message.id_message)?.let { entity ->
                entity.toMessage().let { _entity ->
                    if (_entity.id_message == message.id_message && _entity.username != message.username){
                        source.updateMessage(message.toEntity())
                    }
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
}