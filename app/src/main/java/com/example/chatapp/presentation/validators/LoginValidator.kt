package com.example.chatapp.presentation.validators


interface ILoginValidator : Validator<String>

class LoginValidator : ILoginValidator {
    override fun invoke(form: String): ValidateResult =
        when {
            form.isEmpty() -> ValidateResult(
                tag = "login",
                isSuccessful = false,
                message = "Поле не может быть пустым"
            )
            form.length < 5 -> ValidateResult(
                tag = "login",
                isSuccessful = false,
                message = "Логин должен быть не менее 6 символов"
            )
            else -> ValidateResult(
                tag = "login",
            )
        }
}