package com.example.chatapp.data.repositoryImpl

import android.util.Log
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.domain.models.Message
import com.example.chatapp.domain.repository.IMessagesRepository
import retrofit2.HttpException

class MessagesRepository(
    private val api: IAuthApi
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
}