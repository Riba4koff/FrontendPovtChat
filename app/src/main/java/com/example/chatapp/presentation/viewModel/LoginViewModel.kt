package com.example.chatapp.presentation.viewModel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.LoginResult
import com.example.chatapp.domain.repository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: IUserRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onLoginChange(text: String) {
        viewModelScope.launch {
            _loginState.update {
                it.copy(login = text)
            }
        }
    }

    fun onPasswordChange(text: String) {
        viewModelScope.launch {
            _loginState.update {
                it.copy(password = text)
            }
        }
    }

    fun tryLogin(
        navigateToChats: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {////////////////////////////////////////////////////////////////////////////////////////////////////////////\
            enableLoading()
            val result = repository.signIn(
                login = loginState.value.login.trim(),
                password = loginState.value.password.trim()
            )
            disableLoading()

            when (result) {
                is LoginResult.Authorized -> {
                    Toast.makeText(context, "Вы успешно авторизовались", Toast.LENGTH_SHORT).show()
                    navigateToChats()
                }
                is LoginResult.Unauthorized -> {
                    Toast.makeText(context, "Вы не авторизованы", Toast.LENGTH_SHORT).show()
                }
                is LoginResult.InvalidLoginOrPassword -> {
                    Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
                is LoginResult.HttpError -> {
                    Toast.makeText(context, "Сетевая ошибка", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun enableLoading() {
        _loginState.update {
            it.copy(
                inProcessing = true,
                tryEnabledLogin = false
            )
        }
    }

    private fun disableLoading() {
        _loginState.update {
            it.copy(
                inProcessing = false,
                tryEnabledLogin = true
            )
        }
    }
}
