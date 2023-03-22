package com.example.chatapp.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.Profile.ProfileState
import kotlinx.coroutines.launch

abstract class IProfileViewModel(
    initialState : ProfileState
) : MVIViewModel<ProfileState>(initialState){
    abstract fun logout(context : Context, exit: () -> Unit)
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

    override fun logout(
        context: Context,
        exit: () -> Unit
    ) {
        viewModelScope.launch {
            repository.logout().let { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            exit()
        }
    }
}
