package com.example.chatapp.domain.irepository

import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoResponse
import com.example.chatapp.data.util.LoginResult
import com.example.chatapp.data.util.RegisterResult
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.User

interface IUserRepository {
    suspend fun signUp(login: String, password: String, username: String, email: String): RegisterResult<String>
    suspend fun signIn(login: String, password: String): LoginResult<String>
    suspend fun logout() : Result<String>
    suspend fun authenticate(signInResponse: SignInResponse) : LoginResult<String>
    suspend fun getUser() : Result<User>
    suspend fun editUser(editUserInfoRequest: EditUserInfoRequest) : Result<String>
}