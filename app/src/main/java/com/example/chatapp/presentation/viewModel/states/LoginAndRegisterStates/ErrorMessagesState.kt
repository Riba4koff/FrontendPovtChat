package com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates

import com.example.chatapp.presentation.validators.ValidateResult

data class ErrorMessagesState(
    val loginError: ValidateResult = ValidateResult(),
    val emailError: ValidateResult = ValidateResult(),
    val usernameError: ValidateResult = ValidateResult(),
    val passwordError: ValidateResult = ValidateResult(),
    val invalidLoginOrPassword: ValidateResult = ValidateResult(),
    val unknownError: ValidateResult = ValidateResult(),
    val userHasAlreadyExists: ValidateResult = ValidateResult()
)

