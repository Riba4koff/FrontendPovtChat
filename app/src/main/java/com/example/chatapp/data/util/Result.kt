package com.example.chatapp.data.util

sealed class Result<T>(val data: T? = null, val message: String ?= null) {
    class Success<T>(data: T? = null, message: String?= null) : Result<T>(data, message)
    class Error<T>(data: T? = null, message: String ?= null) : Result<T>(data, message)
}
