package com.example.chatapp.data.util

sealed class LoginResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): LoginResult<T>(data)
    class Unauthorized<T>(data: T? = null): LoginResult<T>(data)
    class InvalidLoginOrPassword<T>(data: T? = null) : LoginResult<T>(data)
    class HttpError<T>(data: T? = null): LoginResult<T>(data)
}
