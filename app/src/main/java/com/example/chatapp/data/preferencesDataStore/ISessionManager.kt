package com.example.chatapp.data.preferencesDataStore

import kotlinx.coroutines.flow.Flow


interface ISessionManager {
    suspend fun saveJwtToken(token: String)

    suspend fun updateSession(token: String, login: String, email: String, username: String)

    suspend fun getJwtToken(): Flow<String>
    suspend fun getCurrentLogin(): Flow<String>
    suspend fun getCurrentEmail(): Flow<String>
    suspend fun getCurrentUsername(): Flow<String>

    suspend fun editUserInfo(login: String, email: String, username: String)

    suspend fun logout()
}