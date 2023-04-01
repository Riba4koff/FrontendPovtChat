package com.example.chatapp.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.User.EditProfileState
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
            repository.getUser().let { result ->
                result.onSuccess { user ->
                    reduce {
                        state.copy(
                            login = user.login,
                            username = user.username,
                            email = user.email
                        )
                    }
                }.onFailure { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
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
            repository.getUser().let { result ->
                result.onSuccess { user ->
                    repository.editUser(
                        EditUserInfoRequest(
                            oldLogin = user.login,
                            newLogin = state.login,
                            oldUsername = user.username,
                            newUsername = state.username,
                            email = state.email
                        )
                    ).let { result ->
                        when (result) {
                            is Result.Success -> {
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT)
                                    .show()
                                navigateToProfile()
                            }
                            is Result.Error -> {
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }.onFailure { exception ->
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}