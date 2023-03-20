package com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo

@kotlinx.serialization.Serializable
data class EditUserInfoResponse(
    val successful: Boolean = false,
    val message: String = ""
)
