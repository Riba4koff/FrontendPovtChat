package com.example.chatapp.presentation.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.User
import com.example.chatapp.domain.repository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Profile.EditProfileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditUserInfoViewModel(
    private val repository: IUserRepository,
    context: Context
) : ViewModel() {
    private val _editUserInfoState = MutableStateFlow(EditProfileState())
    val state = _editUserInfoState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val queryResult = repository.getUser()) {
                is Result.Success -> {
                    queryResult.data.let { user ->
                        _editUserInfoState.update { state ->
                            state.copy(
                                login = user!!.login,
                                username = user.username,
                                email = user.email
                            )
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(context, queryResult.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onLoginChange(text: String) {
        viewModelScope.launch {
            _editUserInfoState.update { state ->
                state.copy(login = text)
            }
        }
    }

    fun onEmailChange(text: String) {
        viewModelScope.launch {
            _editUserInfoState.update { state ->
                state.copy(email = text)
            }
        }
    }

    fun onUsernameChange(text: String) {
        viewModelScope.launch {
            _editUserInfoState.update { state ->
                state.copy(username = text)
            }
        }
    }

    fun save(
        navigateToProfile: () -> Unit,
        context: Context,
    ) {
        viewModelScope.launch {
            when (val userResult = repository.getUser()) {
                is Result.Success -> {
                    userResult.data.let { user ->
                        when (val editUserResult = repository.editUser(
                            EditUserInfoRequest(
                                oldLogin = user!!.login,
                                newLogin = state.value.login,
                                oldUsername = user.username,
                                newUsername = state.value.username,
                                email = state.value.email
                            )
                        )) {
                            is Result.Success -> {
                                Toast.makeText(context, editUserResult.message, Toast.LENGTH_SHORT)
                                    .show()
                                navigateToProfile()
                            }
                            is Result.Error -> {
                                Toast.makeText(context, editUserResult.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
                is Result.Error -> Toast.makeText(context, userResult.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}