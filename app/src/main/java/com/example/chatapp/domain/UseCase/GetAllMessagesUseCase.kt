package com.example.chatapp.domain.UseCase

import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.models.Message

class GetAllMessagesUseCase(private val messagesRepository: IMessagesRepository) {
    suspend fun execute(error: suspend (String) -> Unit): List<Message> {
        //return messagesRepository.getAllMessages().data ?: emptyList()
        return messagesRepository.getAllMessages().let { result ->
            when (result) {
                is Result.Success -> {
                    result.data ?: emptyList()
                }
                is Result.Error -> {
                    error("Ошибка подключения")
                    result.data ?: emptyList()
                }
            }
        }
    }
}