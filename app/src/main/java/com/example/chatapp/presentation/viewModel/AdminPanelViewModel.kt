package com.example.chatapp.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.MessagesRepository
import com.example.chatapp.data.repository.UserRepository
import com.example.chatapp.data.util.Result
import com.example.chatapp.presentation.viewModel.states.User.AdminPanelState
import kotlinx.coroutines.launch

abstract class IAdminViewModel(
    initialState: AdminPanelState
) : MVIViewModel<AdminPanelState>(initialState)

class AdminPanelViewModel(
    private val usersRepository: UserRepository,
    private val messagesRepository: MessagesRepository
) : IAdminViewModel(AdminPanelState()) {
    fun idChange(text: String){
        viewModelScope.launch {
            reduce {
                state.copy(id = text)
            }
        }
    }
    fun deleteAllMessages(context: Context){
        viewModelScope.launch {
            messagesRepository.deleteAllMessages().let { response ->
                when (response) {
                    is Result.Success -> {
                        Toast.makeText(context, "Сообщения удалены", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun deleteAllUsers(context: Context){
        viewModelScope.launch {
            usersRepository.deleteAllUsers().let { response ->
                when (response) {
                    is Result.Success -> {
                        Toast.makeText(context, "Пользователи удалены", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    fun deleteUserById(context: Context){
        viewModelScope.launch {
            usersRepository.deleteUserByLogin(state.id).let { response ->
                when (response) {
                    is Result.Success -> {
                        Toast.makeText(context, response.data?.message ?: "Пользователь удален", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, response.data?.message ?: "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}