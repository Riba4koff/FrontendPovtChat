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

abstract class IChatViewModel(
    initialState: ChatState
) : MVIViewModel<ChatState>(initialState){
    abstract fun connect()
    abstract fun onMessageChange(message: String)
    abstract fun disconnect()
    abstract fun getAllMessages()
    abstract fun sendMessage()
}

class ChatViewModel(
    private val messagesRepository: IMessagesRepository,
    private val chatSocketService: IChatSocketService,
    private val userRepository: IUserRepository,
) : IChatViewModel(ChatState()) {
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser().let {
                when (it) {
                    is Result.Success -> {
                        it.data.let { user ->
                            reduce {
                                state.copy(username = user?.username!!)
                            }
                        }
                    }
                    is Result.Error -> _toastEvent.emit(it.message ?: "unknown error")
                }
            }
        }
    }

    override fun connect() {
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
                                            state.messages.toMutableList().apply {
                                                add(0, message)
                                            }.let { newList ->
                                                reduce {
                                                    state.copy(messages = newList, username = user.username)
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

    override fun onMessageChange(message: String) {
        viewModelScope.launch {
            reduce {
                state.copy(message = message)
            }
        }
    }

    override fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            chatSocketService.closeSession()
        }
    }

    override fun getAllMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            messagesRepository.getAllMessages().let { messages ->
                reduce {
                    state.copy(messages = messages)
                }
            }
        }
    }

    override fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.message.isNotBlank()) {
                chatSocketService.sendMessage(state.message)
                reduce {
                    state.copy(message = "")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}