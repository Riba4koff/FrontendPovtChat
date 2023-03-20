package com.example.chatapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.repository.IMessagesRepository
import com.example.chatapp.domain.repository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Chat.ChatState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatViewModel(
    private val messagesRepository: IMessagesRepository,
    private val chatSocketService: IChatSocketService,
    private val userRepository: IUserRepository,
) : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser().let {
                when (it) {
                    is Result.Success -> {
                        it.data.let { user ->
                            _chatState.update { chat ->
                                chat.copy(username = user?.username!!)
                            }
                        }
                    }
                    is Result.Error -> _toastEvent.emit(it.message ?: "unknown error")
                }
            }
        }
    }

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMessages()
            userRepository.getUser().let { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let { user ->
                            chatSocketService.initSession(user!!.username).let { initResult ->
                                when (initResult) {
                                    is Result.Success -> {
                                        chatSocketService.observeMessages().onEach { message ->
                                            chatState.value.messages.toMutableList().apply {
                                                add(0, message)
                                            }.let { newList ->
                                                _chatState.update { chat ->
                                                    chat.copy(messages = newList, username = user.username)
                                                }
                                            }
                                        }.launchIn(viewModelScope)
                                    }
                                    is Result.Error -> {_toastEvent.emit(result.message ?: "unknown error")}
                                }
                            }
                        }
                    }
                    is Result.Error -> _toastEvent.emit(result.message ?: "unknown error")
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        viewModelScope.launch {
            _chatState.update { chat ->
                chat.copy(message = message)
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            chatSocketService.closeSession()
        }
    }

    private fun getAllMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            _chatState.update { chat ->
                messagesRepository.getAllMessages().let { messages ->
                    chat.copy(messages = messages)
                }
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            if (chatState.value.message.isNotBlank()) {
                chatSocketService.sendMessage(_chatState.value.message)
                _chatState.update { chat -> chat.copy(message = "") }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}