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

abstract class IEditUserInfoViewModel(
    initialState: EditProfileState,
) : MVIViewModel<EditProfileState>(initialState) {
    abstract fun onLoginChange(text: String)
    abstract fun onUsernameChange(text: String)
    abstract fun onEmailChange(text: String)
    abstract fun save(navigateToProfile: () -> Unit, context: Context)
}

class EditUserInfoViewModel(
    private val repository: IUserRepository,
    context: Context,
) : IEditUserInfoViewModel(EditProfileState()) {

    init {
        viewModelScope.launch {
            when (val queryResult = repository.getUser()) {
                is Result.Success -> {
                    queryResult.data.let { user ->
                        reduce {
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

    override fun onLoginChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(login = text)
            }
        }
    }

    override fun onEmailChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(email = text)
            }
        }
    }

    override fun onUsernameChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(username = text)
            }
        }
    }

    override fun save(
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
                                newLogin = state.login,
                                oldUsername = user.username,
                                newUsername = state.username,
                                email = state.email
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