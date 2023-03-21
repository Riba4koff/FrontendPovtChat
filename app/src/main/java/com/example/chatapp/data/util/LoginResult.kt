package com.example.chatapp.data.util

sealed class LoginResult<T>(val data: T? = null, val message: String ?= null) {
    class Authorized<T>(data: T? = null): LoginResult<T>(data)
    class Unauthorized<T>(data: T? = null): LoginResult<T>(data)
    class InvalidLoginOrPassword<T>(data: T? = null) : LoginResult<T>(data)
    class Error<T>(data: T? = null, message: String ?= null): LoginResult<T>(data, message)
}
