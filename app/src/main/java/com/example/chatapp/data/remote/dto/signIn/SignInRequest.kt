package com.example.chatapp.data.remote.dto.signIn

@kotlinx.serialization.Serializable
data class SignInRequest(
    val login: String,
    val password: String
)
