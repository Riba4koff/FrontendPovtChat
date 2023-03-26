package com.example.chatapp.data.remote.ChatSocketService

import android.util.Log
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.dto.messages.MessageDTO
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class ChatSocketService(
    private val api: IAuthApi,
) : IChatSocketService {
    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): Result<Unit> {
        return try {
            socket = api.initWebSocketSession(username)
            if (socket?.isActive == true) {
                Result.Success()
            } else {
                Result.Error("Couldn`t establish a connection")
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.localizedMessage ?: "Some error was occurred.")
        }
        catch (e: SocketTimeoutException){
            Result.Error(e.message ?: "Ошибка подключения к серверу")
        }
        catch (e: ConnectTimeoutException){
            Result.Error(e.message ?: "Ошибка подключения к серверу")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map { frame ->
                    (frame as? Frame.Text)?.readText()!!.let { json ->
                        Json.decodeFromString<MessageDTO>(json).toMessage()
                    }
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }
}