package com.example.chatapp.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.data.util.MessagesResult
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Chat.ChatState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
            userRepository.getUser().let { result ->
                when (result) {
                    is Result.Success -> {
                        getAllMessages()
                        result.data.let { user ->
                            chatSocketService.initSession(user!!.username).let { initResult ->
                                when (initResult) {
                                    is Result.Success -> {
                                        if (countSuccessConnections == 0) _toastEvent.emit(result.message ?: "Подключение установлено")
                                        countSuccessConnections++
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
                                    is Result.Error -> {
                                        countSuccessConnections = 0
                                        _toastEvent.emit(result.message ?: "Подключение разорвано, отправка сообщений невозможна")
                                    }
                                }
                            }
                        }
                    }
                    is Result.Error -> _toastEvent.emit(result.message ?: "Ошибка данных пользователя")
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
            startLoading()
            messagesRepository.getAllMessages().let { result ->
                when (result) {
                    is MessagesResult.Success -> {
                        stopLoading()
                        reduce {
                            state.copy(messages = result.data ?: emptyList())
                        }
                    }
                    is MessagesResult.Error -> {
                        stopLoading()
                        reduce {
                            state.copy(messages = result.data ?: emptyList())
                        }
                    }
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

    fun startLoading() {
        reduce {
            state.copy(isLoading = true)
        }
    }
    fun stopLoading() {
        reduce {
            state.copy(isLoading = false)
        }
    }


}
private var countSuccessConnections = 0