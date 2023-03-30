package com.example.chatapp.data.remote.KtorClient

import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.authenticate.AuthenticateResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllMessagesResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllUsersResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteUserByLoginResponse
import com.example.chatapp.data.remote.dto.messages.MessageDTO
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.Message
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException

class AuthApi(private val client: KtorHttpClient) : IAuthApi {
    override suspend fun signIn(request: SignInRequest): SignInResponse =
        withContext(Dispatchers.IO) {
            client.client.post {
                url(IAuthApi.Endpoints.Login.url)
                body = request
            }
        }

    override suspend fun signUp(request: SignUpRequest): SignUpResponse =
        withContext(Dispatchers.IO) {
            client.client.post {
                url(IAuthApi.Endpoints.Register.url)
                body = request
            }
        }

    override suspend fun authenticate(): AuthenticateResponse =
        withContext(Dispatchers.IO) {
            client.client.post {
                url(IAuthApi.Endpoints.Authenticate.url)
            }
        }

    override suspend fun initWebSocketSession(username: String) =
        withContext(Dispatchers.IO) {
            client.client.webSocketSession {
                url("${IAuthApi.Endpoints.ChatSocket.url}?username=$username")
            }
        }

    override suspend fun editUser(editUserInfoRequest: EditUserInfoRequest): EditUserInfoResponse =
        withContext(Dispatchers.IO) {
            client.client.post {
                url(IAuthApi.Endpoints.EditUser.url)
                body = editUserInfoRequest
            }
        }

    override suspend fun deleteUserByLogin(login: String): Result<DeleteUserByLoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                client.client.post<DeleteUserByLoginResponse> {
                    url(IAuthApi.Endpoints.DeleteUserByLogin.url + "?login=$login")
                }.let { response ->
                    Result.Success(data = response)
                }
            } catch (e: ConnectTimeoutException) {
                Result.Error(message = "Время подключения к серверу истекло")
            } catch (e: ConnectException) {
                Result.Error(message = "Ошибка подключения к серверу")
            } catch (e: HttpException) {
                Result.Error(message = "Сетевая ошибка")
            } catch (e: Exception) {
                Result.Error(message = e.message.toString())
            }
        }

    override suspend fun deleteAllUsers(): Result<DeleteAllUsersResponse> =
        withContext(Dispatchers.IO) {
            try {
                client.client.post<DeleteAllUsersResponse> {
                    url(IAuthApi.Endpoints.DeleteAllUsers.url)
                }.let { response ->
                    Result.Success(data = response)
                }
            } catch (e: ConnectTimeoutException) {
                Result.Error(message = "Время подключения к серверу истекло")
            } catch (e: ConnectException) {
                Result.Error(message = "Ошибка подключения к серверу")
            } catch (e: HttpException) {
                Result.Error(message = "Сетевая ошибка")
            } catch (e: Exception) {
                Result.Error(message = e.message.toString())
            }
        }

    override suspend fun deleteAllMessages(): Result<DeleteAllMessagesResponse> =
        withContext(Dispatchers.IO) {
            try {
                client.client.post<DeleteAllMessagesResponse> {
                    url(IAuthApi.Endpoints.DeleteAllMessages.url)
                }.let { response ->
                    Result.Success(data = response)
                }
            } catch (e: ConnectTimeoutException) {
                Result.Error(message = "Время подключения к серверу истекло")
            } catch (e: ConnectException) {
                Result.Error(message = "Ошибка подключения к серверу")
            } catch (e: HttpException) {
                Result.Error(message = "Сетевая ошибка")
            } catch (e: Exception) {
                Result.Error(message = e.message.toString())
            }
        }

    override suspend fun getAllMessages(): Result<List<Message>> =
        withContext(Dispatchers.IO) {
            try {
                client.client.get<List<MessageDTO>> {
                    url(IAuthApi.Endpoints.GetMessages.url)
                }.sortedByDescending {
                    it.time_sending
                }.map { messageDTO ->
                    messageDTO.toMessage()
                }.let { messages ->
                    Result.Success(data = messages)
                }
            } catch (e: ConnectTimeoutException) {
                Result.Error(message = "Время подключения к серверу истекло")
            } catch (e: ConnectException) {
                Result.Error(message = "Ошибка подключения к серверу")
            } catch (e: HttpException) {
                Result.Error(message = "Сетевая ошибка")
            } catch (e: Exception) {
                Result.Error(message = e.message.toString())
            }
        }
}