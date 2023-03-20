package com.example.chatapp.presentation.validators

interface ICredentialValidator : Validator<String>

class CredentialValidator : ICredentialValidator {
    override fun invoke(form: String): ValidateResult =
        when(form.isEmpty()) {
            true -> ValidateResult(
                tag = "credential",
                isSuccessful = false,
                message = "Поле не может быть пустым"
            )
            false -> ValidateResult(
                tag = "credential"
            )
        }

}
