package com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp

@kotlinx.serialization.Serializable
data class SignUpResponse(
    val successful: Boolean,
    val userHasAlreadyExists: Boolean,
    val message: String
)
