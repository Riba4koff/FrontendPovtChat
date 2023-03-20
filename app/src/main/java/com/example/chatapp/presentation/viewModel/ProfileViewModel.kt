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

class ProfileViewModel(
    private val repository: IUserRepository,
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = repository.getUser()) {
                is Result.Success -> {
                    result.data.let { user ->
                        _profileState.update { state ->
                            state.copy(
                                username = user!!.username,
                                login = user.login,
                                email = user.email
                            )
                        }
                    }
                }
                is Result.Error -> {
                    _profileState.update { state ->
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

    fun logout(exit: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            exit()
        }
    }

}
