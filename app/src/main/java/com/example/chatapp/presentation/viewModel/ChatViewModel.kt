package com.example.chatapp.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.domain.UseCase.ConnectUseCase
import com.example.chatapp.domain.UseCase.GetAllMessagesUseCase
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Chat.ChatState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class IChatViewModel(
    initialState: ChatState
) : MVIViewModel<ChatState>(initialState) {
    abstract fun connect()
    abstract fun loadMessages()
    abstract fun onMessageChange(message: String)
    abstract fun disconnect()
    abstract fun sendMessage()
    abstract fun showToastMessages(context: Context)
    abstract fun connectionManagement(lifecycleOwner: LifecycleOwner): LifecycleEventObserver

}

class ChatViewModel(
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

    //Called when a chat activity is started and
    //it needs to observe to socket.incoming to receive new message
    override fun connect() {
        viewModelScope.launch {
            userRepository.getUser().getOrNull()?.let { user ->
                connectUseCase.execute(
                    username = user.username,
                    chatSocketService = chatSocketService
                ) { error ->
                    _toastEvent.emit(error)
                }
            }
        }
    }

    //observes and load on receive new messages
    override fun loadMessages() {
        viewModelScope.launch {
            getAllMessages.execute(
                resultOfReceivingMessages = { result ->
                    _toastEvent.emit(result)
                }
            ) { messages ->
                reduce { state.copy(messages = messages) }
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

    //This method sends a message to the server and other users
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

    //Shows various error and success messages
    override fun showToastMessages(context: Context) {
        viewModelScope.launch {
            toastEvent.collectLatest { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }


    //Manages connection to chat and downloading of messages
    override fun connectionManagement(lifecycleOwner: LifecycleOwner): LifecycleEventObserver {
        return LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                loadMessages()
                connect()
            }
            else if (event == Lifecycle.Event.ON_STOP) {
                disconnect()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
