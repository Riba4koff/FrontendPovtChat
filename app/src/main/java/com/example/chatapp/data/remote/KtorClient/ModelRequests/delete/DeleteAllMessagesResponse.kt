package com.example.chatapp.data.remote.KtorClient.ModelRequests.delete

@kotlinx.serialization.Serializable
data class DeleteAllMessagesResponse(
    val successful: Boolean,
    val message: String
)
