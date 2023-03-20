package com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates

data class LoginState(
    val login: String = "",
    val password: String = "",
    val inProcessing: Boolean = false,
    val tryEnabledLogin: Boolean = true
)
