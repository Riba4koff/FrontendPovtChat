package com.example.chatapp.presentation.viewModel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.LoginResult
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.LoginState
import kotlinx.coroutines.launch

abstract class ILoginViewModel(
    initialState: LoginState
) : MVIViewModel<LoginState>(initialState){
    abstract fun onLoginChange(text: String)
    abstract fun onPasswordChange(text: String)
    abstract fun tryLogin(navigateToChats: () -> Unit, context: Context)
    abstract fun enableLoading()
    abstract fun disableLoading()
}

class LoginViewModel(
    private val repository: IUserRepository,
) : ILoginViewModel(LoginState()) {

    override fun onLoginChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(login = text)
            }
        }
    }

    override fun onPasswordChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(password = text)
            }
        }
    }

    override fun tryLogin(
        navigateToChats: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {////////////////////////////////////////////////////////////////////////////////////////////////////////////\
            enableLoading()
            val result = repository.signIn(
                login = state.login.trim(),
                password = state.password.trim()
            )
            disableLoading()

            when (result) {
                is LoginResult.Authorized -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                    navigateToChats()
                }
                is LoginResult.Unauthorized -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is LoginResult.InvalidLoginOrPassword -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
                is LoginResult.Error -> {
                    Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun enableLoading() {
        reduce {
            state.copy(
                inProcessing = true,
                tryEnabledLogin = false
            )
        }
    }

    override fun disableLoading() {
        reduce {
            state.copy(
                inProcessing = false,
                tryEnabledLogin = true
            )
        }
    }
}
