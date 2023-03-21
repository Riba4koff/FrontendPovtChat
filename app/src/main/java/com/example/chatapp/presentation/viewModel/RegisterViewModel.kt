package com.example.chatapp.presentation.viewModel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.util.RegisterResult
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.ErrorMessagesState
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.RegisterState
import com.example.chatapp.presentation.validators.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class IRegisterViewModel(
    initialState: RegisterState
) : MVIViewModel<RegisterState>(initialState) {
    abstract fun onEmailChange(text: String)
    abstract fun onLoginChange(text: String)
    abstract fun onUsernameChange(text: String)
    abstract fun onPasswordChange(text: String)
    abstract fun trySignUp(navigateToChats: () -> Unit, context: Context)
    abstract fun tryRegisterDisable()
    abstract fun tryRegisterEnable()
}

class RegisterViewModel(
    private val emailValidator: EmailValidator,
    private val loginValidator: LoginValidator,
    private val credentialValidator: CredentialValidator,
    private val passwordValidator: PasswordValidator,
    private val repository: IUserRepository,
) : IRegisterViewModel(RegisterState()) {

    private val _errorMessagesState = MutableStateFlow(ErrorMessagesState())
    val errorMessageState = _errorMessagesState.asStateFlow()

    override fun onEmailChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(email = text)
            }
            _errorMessagesState.update { errors ->
                errors.copy(
                    emailError = emailValidator(text)
                )
            }
        }
    }

    override fun onLoginChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(login = text)
            }
            _errorMessagesState.update { errors ->
                errors.copy(
                    loginError = loginValidator(text)
                )
            }
        }
    }

    override fun onUsernameChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(username = text)
            }
            _errorMessagesState.update {
                it.copy(
                    usernameError = credentialValidator(text)
                )
            }
        }
    }

    override fun onPasswordChange(text: String) {
        viewModelScope.launch {
            reduce {
                state.copy(password = text)
            }
            _errorMessagesState.update {
                it.copy(
                    passwordError = passwordValidator(text)
                )
            }
        }
    }

    override fun trySignUp(
        navigateToChats: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {
            tryRegisterDisable()

            val emailResult = emailValidator(state.email)
            val loginResult = loginValidator(state.login)
            val usernameResult = credentialValidator(state.username)
            val passwordResult = passwordValidator(state.password)

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
                        login = state.login,
                        password = state.password,
                        email = state.email,
                        username = state.username,
                    )
                    when (response) {
                        is RegisterResult.Success -> {
                            Toast.makeText(context, response.data, Toast.LENGTH_SHORT).show()
                            repository.signIn(
                                state.login,
                                state.password
                            )
                            navigateToChats()
                        }
                        is RegisterResult.Error -> {
                            viewModelScope.launch {
                                Toast.makeText(context, response.data, Toast.LENGTH_SHORT).show()
                            }
                        }
                        is RegisterResult.UserHasAlreadyExists -> {
                            viewModelScope.launch {
                                Toast.makeText(context, response.data, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    tryRegisterEnable()
                }
            }
        }
    }

    override fun tryRegisterDisable() {
        reduce {
            state.copy(inProcessing = true, tryRegisterEnabled = false)
        }
    }

    override fun tryRegisterEnable() {
        reduce {
            state.copy(inProcessing = false, tryRegisterEnabled = true)
        }
    }
}