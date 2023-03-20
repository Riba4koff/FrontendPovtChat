package com.example.chatapp.presentation.validators

import android.util.Patterns

interface IEmailValidator : Validator<String>

class EmailValidator : IEmailValidator {
    override fun invoke(form: String): ValidateResult =
        when (Patterns.EMAIL_ADDRESS.matcher(form).matches()) {
            true -> ValidateResult(
                tag = "email"
            )
            false -> ValidateResult(
                tag = "email",
                isSuccessful = false,
                message = "Почта введена неккоректно"
            )
        }
}