package com.example.chatapp.domain.UseCase

import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.models.Message
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class GetAllMessagesUseCase(private val messagesRepository: IMessagesRepository) {
    suspend fun execute(
        resultOfReceivingMessages: suspend (String) -> Unit,
        sendMessages: suspend (List<Message>) -> Unit
    ) {
        messagesRepository.getAllMessages().let { result ->
            resultOfReceivingMessages(result.message ?: "Неизвестная ошибка")
            result.data?.map { messages ->
                sendMessages(messages.reversed())
            }?.collect()
        }
    }
}