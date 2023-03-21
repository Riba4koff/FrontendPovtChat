package com.example.chatapp.data.util


sealed class RegisterResult<T>(val data: T? = null, val message: String ?= null) {
    class Success<T>(data: T? = null) : RegisterResult<T>(data)
    class UserHasAlreadyExists<T>(data: T? = null) : RegisterResult<T>(data)
    class Error<T>(data: T? = null, message: String ?= null) : RegisterResult<T>(data, message)
}