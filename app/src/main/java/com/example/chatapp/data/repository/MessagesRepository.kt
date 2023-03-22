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
        return try {
            api.getAllMessages().map { message ->
                source.fetchMessageById(message.id_message).let { entity ->
                    if (entity == null) source.insertMessage(message = message.toEntity())
                    entity?.toMessage()?.let { messageDB ->
                        if (messageDB.id_message == message.id_message && message.username != messageDB.username) source.updateMessage(
                            message = message.toEntity()
                        )
                    }
                }
            }
            MessagesResult.Success(source.fetchAllMessages().reversed().map{ it.toMessage() })
        } catch (e: ConnectTimeoutException) {
            MessagesResult.Success(source.fetchAllMessages().reversed().map {
                it.toMessage()
            }, message = "Время подключения к серверу истекло")
        } catch (e: ConnectException) {
            MessagesResult.Success(source.fetchAllMessages().reversed().map {
                it.toMessage()
            }, message = "Ошибка подключения к серверу")
        } catch (e: HttpException) {
            MessagesResult.Success(source.fetchAllMessages().reversed().map {
                it.toMessage()
            }, message = "Сетевая ошибка")
        } catch (e: Exception) {
            MessagesResult.Error(emptyList(), e.message.toString())
        }
    }

    override suspend fun insertMessage(message: Message) {
        source.insertMessage(message = message.toEntity())
    }

    override suspend fun updateMessage(message: Message) {
        source.updateMessage(message = message.toEntity())
    }
}