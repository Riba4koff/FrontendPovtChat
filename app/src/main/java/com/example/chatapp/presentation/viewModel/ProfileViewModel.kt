package com.example.chatapp.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.User.ProfileState
import kotlinx.coroutines.launch

abstract class IProfileViewModel(
    initialState : ProfileState
) : MVIViewModel<ProfileState>(initialState){
    abstract fun logout(context : Context, navigateToLogin: () -> Unit)
}

class ProfileViewModel(
    private val repository: IUserRepository,
) :IProfileViewModel(ProfileState()) {
    init {
        viewModelScope.launch {
            repository.getUser().let { result ->
                result.onSuccess { user ->
                    reduce {
                        state.copy(
                            username = user.username,
                            login = user.login,
                            email = user.email
                        )
                    }
                }.onFailure {
                    reduce {
                        state.copy(
                            username = "Null",
                            login = "Null",
                            email = "Null"
                        )
                    }
                }
            }
        }
    }

    override fun logout(
        context: Context,
        navigateToLogin: () -> Unit
    ) {
        viewModelScope.launch {
            repository.logout().let { result ->
                result.onSuccess {
                    Toast.makeText(context, "Вы успешно вышли", Toast.LENGTH_SHORT).show()
                }.onFailure {
                    Toast.makeText(context, "Ошибка выхода", Toast.LENGTH_SHORT).show()
                }
            }
            navigateToLogin()
        }
    }
}
