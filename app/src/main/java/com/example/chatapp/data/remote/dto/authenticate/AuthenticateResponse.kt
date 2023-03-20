package com.example.chatapp.data.remote.dto.authenticate

@kotlinx.serialization.Serializable
data class AuthenticateResponse(
    val message: String,
    val successful: Boolean
)
