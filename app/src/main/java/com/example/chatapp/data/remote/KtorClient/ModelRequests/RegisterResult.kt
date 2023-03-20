package com.example.chatapp.data.remote.KtorClient


sealed class RegisterResult<T>(val data: T? = null) {
    class Success<T>(data: T? = null) : RegisterResult<T>(data)
    class UserHasAlreadyExists<T>(data: T? = null) : RegisterResult<T>(data)
    class Error<T>(data: T? = null) : RegisterResult<T>(data)
}