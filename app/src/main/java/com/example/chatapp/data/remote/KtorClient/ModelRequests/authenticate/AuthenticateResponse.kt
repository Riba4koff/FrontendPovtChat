package com.example.chatapp.data.remote.KtorClient.ModelRequests.authenticate

@kotlinx.serialization.Serializable
data class AuthenticateResponse(
    val message: String,
    val successful: Boolean
)
