package com.example.chatapp.data.remote.dto.signUp

@kotlinx.serialization.Serializable
data class SignUpResponse(
    val successful: Boolean,
    val userHasAlreadyExists: Boolean
)
