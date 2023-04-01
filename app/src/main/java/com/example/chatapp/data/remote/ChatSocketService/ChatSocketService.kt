package com.example.chatapp.data.remote.ChatSocketService

import android.util.Log
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.dto.messages.MessageDTO
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class ChatSocketService(
    private val api: IAuthApi,
) : IChatSocketService {
    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): kotlin.Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                socket = api.initWebSocketSession(username)
            }
        }

    override suspend fun sendMessage(message: String) =
        withContext(Dispatchers.IO) {
            runCatching { socket?.send(Frame.Text(message)) }
        }

    override suspend fun closeSession() {
        withContext(Dispatchers.IO) {
            socket?.close()
        }
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