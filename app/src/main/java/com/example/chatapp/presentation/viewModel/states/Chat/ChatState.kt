package com.example.chatapp.presentation.viewModel.states.Chat

import com.example.chatapp.domain.models.Message

data class ChatState(
    val username: String = "",
    val message: String = "",
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)
