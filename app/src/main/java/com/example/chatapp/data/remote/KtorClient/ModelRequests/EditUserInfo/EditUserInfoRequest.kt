package com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo

@kotlinx.serialization.Serializable
data class EditUserInfoRequest(
    val oldLogin: String = "",
    val newLogin: String = "",
    val oldUsername: String = "",
    val newUsername: String = "",
    val email: String = ""
)
