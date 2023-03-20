package com.example.chatapp.domain.models

data class User(
    var login: String = "ExampleLogin",
    var email: String = "Example@mail.ru",
    val username: String = ""
)
