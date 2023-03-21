package com.example.chatapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.repository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Profile.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class IProfileViewModel(
    initialState : ProfileState
) : MVIViewModel<ProfileState>(initialState){
    abstract fun logout(exit: () -> Unit)
}

class ProfileViewModel(
    private val repository: IUserRepository,
) :IProfileViewModel(ProfileState()) {
    init {
        viewModelScope.launch {
            when (val result = repository.getUser()) {
                is Result.Success -> {
                    result.data.let { user ->
                        reduce {
                            state.copy(
                                username = user!!.username,
                                login = user.login,
                                email = user.email
                            )
                        }
                    }
                }
                is Result.Error -> {
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

    override fun logout(exit: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            exit()
        }
    }
}
