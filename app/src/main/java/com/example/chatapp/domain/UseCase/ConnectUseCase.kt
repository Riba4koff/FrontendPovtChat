package com.example.chatapp.domain.UseCase

import com.example.chatapp.data.remote.ChatSocketService.ChatSocketService
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Thread.State

class ConnectUseCase() {
    suspend fun execute(
        username: String,
        chatSocketService: IChatSocketService,
        sendMessage: suspend (Message) -> Unit
    ) {
        chatSocketService.initSession(username).let { result ->
            result.onSuccess {
                chatSocketService.observeMessages().collect() { message ->
                    sendMessage(message)
                }
            }
        }
    }
}