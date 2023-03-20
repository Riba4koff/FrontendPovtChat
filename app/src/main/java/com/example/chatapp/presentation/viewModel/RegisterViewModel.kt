package com.example.chatapp.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.RegisterResult
import com.example.chatapp.domain.repository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.ErrorMessagesState
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.RegisterState
import com.example.chatapp.presentation.validators.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val emailValidator: EmailValidator,
    private val loginValidator: LoginValidator,
    private val credentialValidator: CredentialValidator,
    private val passwordValidator: PasswordValidator,
    private val repository: IUserRepository,
) : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _errorMessagesState = MutableStateFlow(ErrorMessagesState())
    val errorMessageState = _errorMessagesState.asStateFlow()

    fun onEmailChange(text: String) {
        viewModelScope.launch {
            _registerState.update {
                it.copy(email = text)
            }
            _errorMessagesState.update {
                it.copy(
                    emailError = emailValidator(text)
                )
            }
        }
    }

    fun onLoginChange(text: String) {
        viewModelScope.launch {
            _registerState.update {
                it.copy(login = text)
            }
            _errorMessagesState.update {
                it.copy(
                    loginError = loginValidator(text)
                )
            }
        }
    }

    fun onUsernameChange(text: String) {
        viewModelScope.launch {
            _registerState.update {
                it.copy(username = text)
            }
            _errorMessagesState.update {
                it.copy(
                    usernameError = credentialValidator(text)
                )
            }
        }
    }

    fun onPasswordChange(text: String) {
        viewModelScope.launch {
            _registerState.update {
                it.copy(password = text)
            }
            _errorMessagesState.update {
                it.copy(
                    passwordError = passwordValidator(text)
                )
            }
        }
    }

    fun trySignUp(
        navigateToChats: () -> Unit,
    ) {
        viewModelScope.launch {
            tryRegisterDisable()

            val emailResult = emailValidator(registerState.value.email)
            val loginResult = loginValidator(registerState.value.login)
            val usernameResult = credentialValidator(registerState.value.username)
            val passwordResult = passwordValidator(registerState.value.password)

            val errors = listOf(
                emailResult,
                loginResult,
                usernameResult,
                passwordResult
            ).any {
                it.isSuccessful.not()
            }

            delay(500)

            _errorMessagesState.update {
                it.copy(
                    emailError = emailResult,
                    loginError = loginResult,
                    passwordError = passwordResult
                )
            }

            when (errors) {
                true -> tryRegisterEnable()
                false -> {
                    val response = repository.signUp(
                        login = registerState.value.login,
                        password = registerState.value.password,
                        email = registerState.value.email,
                        username = registerState.value.username,
                    )
                    when (response) {
                        is RegisterResult.Success -> {
                            repository.signIn(
                                _registerState.value.login,
                                _registerState.value.password
                            )
                            navigateToChats()
                        }
                        is RegisterResult.Error -> {
                            viewModelScope.launch {
                                _errorMessagesState.update { state ->
                                    state.copy(
                                        unknownError = ValidateResult(
                                            tag = "UNKNOWN_ERROR",
                                            isSuccessful = false,
                                            message = "Неизвестная ошибка"
                                        )
                                    )
                                }
                            }
                        }
                        is RegisterResult.UserHasAlreadyExists -> {
                            viewModelScope.launch {
                                _errorMessagesState.update { state ->
                                    state.copy(
                                        userHasAlreadyExists = ValidateResult(
                                            tag = "USER_ALREADY_EXISTS",
                                            isSuccessful = false,
                                            message = "Данный пользователь уже зарегистрирован"
                                        )
                                    )
                                }
                            }
                        }
                        else -> TODO()
                    }
                    tryRegisterEnable()
                }
            }
        }
    }

    private fun tryRegisterDisable() {
        _registerState.update {
            it.copy(inProcessing = true, tryRegisterEnabled = false)
        }
    }

    private fun tryRegisterEnable() {
        _registerState.update {
            it.copy(inProcessing = false, tryRegisterEnabled = true)
        }
    }
}