package com.example.chatapp.domain.UseCase

import com.example.chatapp.data.remote.ChatSocketService.ChatSocketService
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.data.repository.MessagesRepository
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Thread.State

class ConnectUseCase(private val messagesRepository: MessagesRepository) {
    suspend fun execute(
        username: String,
        chatSocketService: IChatSocketService,
        error: suspend (String) -> Unit
    ) {
        chatSocketService.initSession(username).let { result ->
            result.onSuccess {
                chatSocketService.observeMessages().collect { message ->
                    messagesRepository.insertMessage(message)
                }
            }
        }.onFailure { _ ->
            error("Ошибка подключения")
        }
    }
}