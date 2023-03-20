package com.example.chatapp.data.remote.dto.signUp

@kotlinx.serialization.Serializable
data class SignUpRequest(
    val login: String,
    val password: String,
    val email: String,
    val username: String
)
