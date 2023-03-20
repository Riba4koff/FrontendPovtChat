package com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn

@kotlinx.serialization.Serializable
data class SignInRequest(
    val login: String,
    val password: String
)
