package com.example.chatapp.presentation.validators

data class ValidateResult(
    val tag: String = "",
    val isSuccessful: Boolean = true,
    val message: String = ""
)

interface Validator<in T> {
    operator fun invoke(form: T): ValidateResult
}
