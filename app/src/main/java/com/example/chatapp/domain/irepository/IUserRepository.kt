package com.example.chatapp.domain.irepository

import com.example.chatapp.data.remote.KtorClient.ModelRequests.EditUserInfo.EditUserInfoRequest
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteAllUsersResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.delete.DeleteUserByLoginResponse
import com.example.chatapp.data.remote.KtorClient.ModelRequests.signIn.SignInResponse
import com.example.chatapp.data.util.Result
import com.example.chatapp.domain.models.User

interface IUserRepository {
    suspend fun signUp(login: String, password: String, username: String, email: String): Result<String>
    suspend fun signIn(login: String, password: String): Result<String>
    suspend fun logout() : kotlin.Result<Unit>
    suspend fun authenticate(signInResponse: SignInResponse) : Result<String>
    suspend fun getUser() : kotlin.Result<User>
    suspend fun editUser(editUserInfoRequest: EditUserInfoRequest) : Result<String>
    suspend fun deleteAllUsers(): Result<DeleteAllUsersResponse>
    suspend fun deleteUserByLogin(login: String): Result<DeleteUserByLoginResponse>
}