package com.example.chatapp.data.remote.KtorClient.ModelRequests.delete

@kotlinx.serialization.Serializable
data class DeleteUserByLoginResponse(
    val successful: Boolean,
    val message: String
)
