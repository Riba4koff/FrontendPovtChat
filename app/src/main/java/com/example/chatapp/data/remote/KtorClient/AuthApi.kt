package com.example.chatapp.data.remote.KtorClient

import android.util.Log
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.authenticate.AuthenticateResponse
import com.example.chatapp.data.remote.dto.messages.MessageDTO
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpResponse
import com.example.chatapp.data.util.MessagesResult
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.network.sockets.*
import retrofit2.HttpException
import java.net.ConnectException

class AuthApi(private val client: KtorHttpClient) : IAuthApi {
    override suspend fun signIn(request: SignInRequest): SignInResponse = client.client.post {
        url(IAuthApi.Endpoints.Login.url)
        body = request
    }

    override suspend fun signUp(request: SignUpRequest): SignUpResponse = client.client.post {
        url(IAuthApi.Endpoints.Register.url)
        body = request
    }

    override suspend fun authenticate(): AuthenticateResponse = client.client.post {
        url(IAuthApi.Endpoints.Authenticate.url)
    }

    override suspend fun initWebSocketSession(username: String) =
        client.client.webSocketSession {
            url("${IAuthApi.Endpoints.ChatSocket.url}?username=$username")
        }

    override suspend fun editUser(editUserInfoRequest: EditUserInfoRequest): EditUserInfoResponse =
        client.client.post {
            url(IAuthApi.Endpoints.EditUser.url)
            body = editUserInfoRequest
        }

    override suspend fun getAllMessages(): MessagesResult<List<Message>> {
        return try {
            client.client.get<List<MessageDTO>> {
                url(IAuthApi.Endpoints.GetMessages.url)
            }.sortedByDescending {
                it.time_sending
            }.map { messageDTO ->
                messageDTO.toMessage()
            }.let { messages ->
                MessagesResult.Success(data = messages)
            }
        } catch (e: ConnectTimeoutException) {
            MessagesResult.Error(message = "Время подключения к серверу истекло")
        } catch (e: ConnectException) {
            MessagesResult.Error(message = "Ошибка подключения к серверу")
        } catch (e: HttpException) {
            MessagesResult.Error(message = "Сетевая ошибка")
        } catch (e: Exception) {
            MessagesResult.Error(message = e.message.toString())
        }
    }
}