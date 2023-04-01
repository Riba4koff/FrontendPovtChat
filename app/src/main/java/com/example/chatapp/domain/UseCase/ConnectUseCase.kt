package com.example.chatapp.domain.UseCase

import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.data.repository.MessagesRepository

class ConnectUseCase(private val messagesRepository: MessagesRepository) {
    suspend fun execute(
        username: String,
        chatSocketService: IChatSocketService,
        error: suspend (String) -> Unit
    ) {
        chatSocketService.initSession(username).let { result ->
            result.onSuccess {
                chatSocketService.observeMessages().collect { message ->
                    messagesRepository.insertMessageFromDataBase(message)
                }
            }
        }.onFailure { _ ->
            error("Ошибка подключения")
        }
    }
}