package com.example.chatapp.data.remote.KtorClient

import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.authenticate.AuthenticateResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllUsersResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteUserByLoginResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import io.ktor.http.cio.websocket.*

interface IAuthApi {
    suspend fun signIn(request: SignInRequest): SignInResponse
    suspend fun signUp(request: SignUpRequest): SignUpResponse
    suspend fun authenticate(): AuthenticateResponse

    suspend fun initWebSocketSession(username: String) : WebSocketSession

    suspend fun editUser(editUserInfoRequest: EditUserInfoRequest): EditUserInfoResponse

    suspend fun getAllMessages() : Result<List<Message>>
    suspend fun deleteUserByLogin(login: String): Result<DeleteUserByLoginResponse>
    suspend fun deleteAllUsers(): Result<DeleteAllUsersResponse>
    suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse>

    companion object {
        private const val BASE_URL = "http://192.168.1.3:8080"
        private const val IPHONE_URL = "http://172.20.10.9:8080"
        private const val WEBSOCKET_URL = "ws://192.168.1.3:8080"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$WEBSOCKET_URL/chat-socket")
        object Login: Endpoints("$BASE_URL/signin")
        object Register: Endpoints("$BASE_URL/signup")
        object Authenticate: Endpoints("$BASE_URL/authenticate")
        object EditUser: Endpoints("$BASE_URL/edit-user-info")
        object GetMessages: Endpoints("$BASE_URL/messages")
        object DeleteAllMessages: Endpoints("$BASE_URL/Delete/messages")
        object DeleteAllUsers: Endpoints("$BASE_URL/Delete/Users")
        object DeleteUserByLogin: Endpoints("$BASE_URL/Delete/User")
    }

}

