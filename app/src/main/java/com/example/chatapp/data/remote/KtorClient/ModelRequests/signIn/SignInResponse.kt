package com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn

@kotlinx.serialization.Serializable
data class SignInResponse(
    val login: String,
    val email: String,
    val username: String,
    val token: String,
    val invalidLoginOrPassword: Boolean
)
