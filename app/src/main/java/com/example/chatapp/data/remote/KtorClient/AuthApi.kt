package com.example.chatapp.data.remote.KtorClient

import android.util.Log
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoResponse
import com.example.chatapp.data.remote.dto.authenticate.AuthenticateResponse
import com.example.chatapp.data.remote.dto.messages.MessageDTO
import com.example.chatapp.data.remote.dto.signIn.SignInRequest
import com.example.chatapp.data.remote.dto.signIn.SignInResponse
import com.example.chatapp.data.remote.dto.signUp.SignUpRequest
import com.example.chatapp.data.remote.dto.signUp.SignUpResponse
import com.example.chatapp.domain.models.Message
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import retrofit2.HttpException

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

    override suspend fun getAllMessages(): List<Message> {
        return client.client.get<List<MessageDTO>> {
            url(IAuthApi.Endpoints.GetMessages.url)
        }.sortedByDescending {
            it.time_sending
        }.map { messageDTO ->
            messageDTO.toMessage()
        }
    }
}