package com.example.chatapp.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.domain.UseCase.ConnectUseCase
import com.example.chatapp.domain.UseCase.GetAllMessagesUseCase
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Chat.ChatState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class IChatViewModel(
    initialState: ChatState
) : MVIViewModel<ChatState>(initialState) {
    abstract fun connect()
    abstract fun onMessageChange(message: String)
    abstract fun disconnect()
    abstract fun sendMessage()
}

class ChatViewModel(
    private val messagesRepository: IMessagesRepository,
    private val chatSocketService: IChatSocketService,
    private val userRepository: IUserRepository,
    private val connectUseCase: ConnectUseCase,
    private val getAllMessages: GetAllMessagesUseCase
) : IChatViewModel(ChatState()) {
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            userRepository.getUser().let { result ->
                result.onSuccess { user ->
                    reduce { state.copy(username = user.username) }
                }
                result.onFailure { exception ->
                    _toastEvent.emit(exception.message ?: "Unknown error.")
                }
            }
        }
    }

    override fun connect() {
        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }

            getAllMessages.execute { error ->
                _toastEvent.emit(error)
            }.let { messages ->
                reduce {
                    state.copy(messages = messages)
                }
            }
            
            reduce { state.copy(isLoading = false) }

            userRepository.getUser().getOrNull()?.let { user ->
                connectUseCase.execute(
                    username = user.username,
                    chatSocketService = chatSocketService
                ) { message ->
                    state.messages.toMutableList().apply {
                        add(0, message)
                    }.let { messages ->
                        messagesRepository.updateMessages(messages)
                        messagesRepository.insertMessage(message)
                        reduce {
                            state.copy(messages = messages)
                        }
                    }
                }
            }
        }
    }

    override fun onMessageChange(message: String) {
        viewModelScope.launch {
            reduce {
                state.copy(message = message)
            }
        }
    }

    override fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    override fun sendMessage() {
        viewModelScope.launch {
            if (state.message.isNotBlank()) {
                chatSocketService.sendMessage(state.message).let { result ->
                    result.onFailure {
                        _toastEvent.emit("Сообщение не отправлено")
                    }
                }
                reduce { state.copy(message = "") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
