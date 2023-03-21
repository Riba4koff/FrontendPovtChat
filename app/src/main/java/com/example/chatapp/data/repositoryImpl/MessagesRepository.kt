package com.example.chatapp.data.repositoryImpl

import android.util.Log
import com.example.chatapp.data.local.room.datasources.IMessagesSource
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.repository.IMessagesRepository
import retrofit2.HttpException

class MessagesRepository(
    private val api: IAuthApi,
    private val source: IMessagesSource
): IMessagesRepository {
    override suspend fun getAllMessages(): List<Message> {
        return try {
            api.getAllMessages()
        } catch (e: Exception){
            Log.d("GET_ALL_MESSAGES_TAG: ", e.message.toString())
            emptyList()
        } catch (e: HttpException){
            Log.d("GET_ALL_MESSAGES_TAG: ", e.message.toString())
            emptyList()
        }
    }

    override suspend fun insertMessage(message: Message) {
        source.insertMessage(message = message.toEntity())
    }

    override suspend fun updateMessage(message: Message) {
        source.updateMessage(message = message.toEntity())
    }
}