package com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates

data class RegisterState(
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val username: String = "",
    val inProcessing: Boolean = false,
    val tryRegisterEnabled: Boolean = true,
)
