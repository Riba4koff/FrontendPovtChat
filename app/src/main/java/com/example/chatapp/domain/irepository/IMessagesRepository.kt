package com.example.chatapp.domain.irepository

import com.example.chatapp.domain.models.Message

interface IMessagesRepository {
     suspend fun getAllMessages() : List<Message>
     suspend fun insertMessage(message: Message)
     suspend fun updateMessage(message: Message)
}