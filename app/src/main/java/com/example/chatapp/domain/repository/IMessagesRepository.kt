package com.example.chatapp.domain.repository

import com.example.chatapp.domain.models.Message

interface IMessagesRepository {
     suspend fun getAllMessages() : List<Message>
}