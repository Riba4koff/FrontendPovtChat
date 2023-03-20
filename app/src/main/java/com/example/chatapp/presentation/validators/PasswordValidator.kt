package com.example.chatapp.presentation.validators

interface IPasswordValidator : Validator<String>

class PasswordValidator : IPasswordValidator{
    override fun invoke(form: String): ValidateResult =
        when {
            form.isEmpty() -> ValidateResult(
                tag = "password",
                isSuccessful = false,
                message = "Поле не может быть пустым"
            )
            form.length < 6 -> ValidateResult(
                tag = "password",
                isSuccessful = false,
                message = "Пароль должен содержать не менее 6 символов"
            )
            else -> ValidateResult(
                tag = "password",
            )
        }
}