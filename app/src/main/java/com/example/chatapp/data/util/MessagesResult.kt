package com.example.chatapp.data.util

sealed class MessagesResult<T>(val data: T?= null, val message: String?= null){
    class Success<T>(data: T? = null, message: String?= null) : MessagesResult<T>(data, message)
    class Error<T>(data: T? = null, message: String?= null) : MessagesResult<T>(data, message)
}
