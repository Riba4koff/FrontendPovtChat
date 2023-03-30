package com.example.chatapp.data.repository

import com.example.chatapp.data.preferencesDataStore.SessionManager
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllUsersResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteUserByLoginResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpRequest
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.User
import com.example.chatapp.domain.irepository.IUserRepository
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

class UserRepository(
    private val api: IAuthApi,
    private val sessionManager: SessionManager
) : IUserRepository, KoinComponent {

    //private val sessionManager by inject<SessionManager>()

    override suspend fun signUp(
        login: String,
        password: String,
        username: String,
        email: String,
    ): Result<String> {
        return try {
            api.signUp(
                SignUpRequest(
                    login, password, email, username
                )
            ).let { response ->
                if (response.successful) Result.Success(response.message)
                else Result.Error(response.message)
            }
        } catch (e: ConnectTimeoutException) {
            Result.Error("Ошибка подключения")
        } catch (e: Exception) {
            Result.Error("Неизвестная ошибка")
        }
    }

    override suspend fun signIn(login: String, password: String): Result<String> {
        return try {
            api.signIn(
                request = SignInRequest(
                    login, password
                )
            ).let { response ->
                if (response.token.isNotBlank()) sessionManager.saveJwtToken(response.token)
                return authenticate(response)
            }
        } catch (e: HttpException) {
            Result.Error(data = "Ошибка сервера")
        } catch (e: ConnectTimeoutException) {
            Result.Error(data = "Нет соединения с сервером")
        } catch (e: Exception) {
            Result.Error(data = "Неизвестная ошибка")
        }
    }

    override suspend fun authenticate(signInResponse: SignInResponse): Result<String> {
        return api.authenticate().let { response ->
            if (response.successful) {
                sessionManager.updateSession(
                    token = signInResponse.token,
                    login = signInResponse.login,
                    email = signInResponse.email,
                    username = signInResponse.username
                )
                Result.Success("Успешная авторизация")
            } else Result.Error("Ошибка авторизации")
        }
    }

    override suspend fun logout(): kotlin.Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            sessionManager.logout()
        }
    }

    override suspend fun getUser(): kotlin.Result<User> = withContext(Dispatchers.IO) {
        runCatching {
            val login = sessionManager.getCurrentLogin().first()
            val email = sessionManager.getCurrentEmail().first()
            val username = sessionManager.getCurrentUsername().first()

            User(login, email, username)
        }.onFailure {
            throw it
        }
    }


    override suspend fun editUser(
        editUserInfoRequest: EditUserInfoRequest,
    ): kotlin.Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val result = api.editUser(editUserInfoRequest)
            if (result.successful) {
                sessionManager.editUserInfo(
                    editUserInfoRequest.newLogin,
                    editUserInfoRequest.email,
                    editUserInfoRequest.newUsername
                )
            }
        }
    }


    override suspend fun deleteAllUsers(): Result<DeleteAllUsersResponse> {
        return api.deleteAllUsers()
    }

    override suspend fun deleteUserByLogin(login: String): Result<DeleteUserByLoginResponse> {
        return api.deleteUserByLogin(login)
    }
}