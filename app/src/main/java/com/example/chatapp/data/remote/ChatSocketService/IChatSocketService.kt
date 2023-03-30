package com.example.chatapp.data.remote.ChatSocketService

import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface IChatSocketService {
    suspend fun initSession(username: String) : Result<Unit>
    suspend fun sendMessage(message: String): Result<Unit?>
    suspend fun closeSession()
    fun observeMessages() : Flow<Message>
}