package com.example.chatapp.data.remote.ChatSocketService

import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface IChatSocketService {
    suspend fun initSession(username: String) : com.example.chatapp.data.util.Result<Unit>
    suspend fun sendMessage(message: String)

    suspend fun closeSession()

    fun observeMessages() : Flow<Message>
}