package com.example.chatapp.data.repositoryImpl

import com.example.chatapp.data.preferencesDataStore.SessionManager
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.util.LoginResult
import com.example.chatapp.data.util.RegisterResult
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signUp.SignUpRequest
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.User
import com.example.chatapp.domain.repository.IUserRepository
import io.ktor.network.sockets.*
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException

class UserRepository(
    private val api: IAuthApi,
) : IUserRepository, KoinComponent {

    private val sessionManager by inject<SessionManager>()

    override suspend fun signUp(
        login: String,
        password: String,
        username: String,
        email: String,
    ): RegisterResult<String> {
        return try {
            api.signUp(
                SignUpRequest(
                    login, password, email, username
                )
            ).let { response ->
                if (response.successful) RegisterResult.Success("Успешная регистрация")
                else if (response.userHasAlreadyExists) RegisterResult.UserHasAlreadyExists("Пользователь уже зарегестрирован")
                else RegisterResult.Error("Неизвестная ошибка")
            }
        } catch (e: ConnectTimeoutException) {
            RegisterResult.Error("Ошибка подключения")
        } catch (e: Exception){
            RegisterResult.Error("Неизвестная ошибка")
        }
    }

    override suspend fun signIn(login: String, password: String): LoginResult<String> {
        return try {
            api.signIn(
                request = SignInRequest(
                    login, password
                )
            ).let { response ->
                if(response.invalidLoginOrPassword) LoginResult.InvalidLoginOrPassword("Неверный логин или пароль")
                else if (response.token.isNotBlank()) sessionManager.saveJwtToken(response.token)
                authenticate(response)
            }
        }
        catch (e: HttpException) {
            LoginResult.Error("Ошибка сервера")
        }
        catch (e: ConnectTimeoutException){
            LoginResult.Error("Ошибка подключения")
        }
        catch (e: Exception){
            LoginResult.Error("Неизвестная ошибка")
        }
    }
    override suspend fun authenticate(signInResponse: SignInResponse): LoginResult<String> {
        return try {
            val response = api.authenticate()
            if (response.successful) {
                sessionManager.updateSession(
                    token = signInResponse.token,
                    login = signInResponse.login,
                    email = signInResponse.email,
                    username = signInResponse.username
                )
                LoginResult.Authorized("Успешная авторизация")
            } else LoginResult.Unauthorized("Ошибка авторизации")
        } catch (e: HttpException) {
            if (e.code() == 401) LoginResult.Unauthorized()
            else LoginResult.Error("Ошибка авторизации")
        } catch (e: Exception) {
            LoginResult.Error("Неизвестная ошибка")
        } catch (e: ConnectTimeoutException) {
            LoginResult.Error("Ошибка подключения")
        }
    }
    override suspend fun logout(): Result<String> {
        return try {
            sessionManager.logout()
            Result.Success("Logged out successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Unknown error occurred.")
        }
    }

    override suspend fun getUser(): Result<User> {
        return try {
            val login = sessionManager.getCurrentLogin().first()
            val email = sessionManager.getCurrentEmail().first()
            val username = sessionManager.getCurrentUsername().first()

            if (login.isBlank() || email.isBlank()) Result.Error("User not Logged In!")
            else Result.Success(User(login, email, username))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Some problem occurred!")
        }

    }

    override suspend fun editUser(
        editUserInfoRequest: EditUserInfoRequest,
    ): Result<String> {
        return try {
            val result = api.editUser(editUserInfoRequest)
            if (result.successful) {
                sessionManager.editUserInfo(
                    editUserInfoRequest.newLogin,
                    editUserInfoRequest.email,
                    editUserInfoRequest.newUsername
                )
                Result.Success(message = result.message)
            } else Result.Error(result.message)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "Some problem occurred!")
        }
    }
}