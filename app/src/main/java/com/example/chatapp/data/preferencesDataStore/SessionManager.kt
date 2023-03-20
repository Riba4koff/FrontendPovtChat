package com.example.chatapp.data.preferencesDataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "preferences_data_store")

class SessionManager(
    context: Context ?= null
) : ISessionManager {

    private val dataStore: DataStore<Preferences> = context!!.dataStore

    companion object KEYS {
        private val TOKEN_ACCESS = stringPreferencesKey("TOKEN_ACCESS")
        private val LOGIN_KEY = stringPreferencesKey("LOGIN_KEY")
        private val EMAIL_KEY = stringPreferencesKey("EMAIL_KEY")
        private val USERNAME_KEY = stringPreferencesKey("USERNAME_KEY")
    }

    override suspend fun updateSession(
        token: String,
        login: String,
        email: String,
        username: String,
    ) {
        dataStore.edit { preferences ->
            preferences[TOKEN_ACCESS] = token
            preferences[LOGIN_KEY] = login
            preferences[EMAIL_KEY] = email
            preferences[USERNAME_KEY] = username
        }
    }

    override suspend fun editUserInfo(
        login: String,
        email: String,
        username: String
    ) {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = login
            preferences[EMAIL_KEY] = email
            preferences[USERNAME_KEY] = username
        }
    }

    override suspend fun saveJwtToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_ACCESS] = token
        }
    }

    override suspend fun getJwtToken(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[TOKEN_ACCESS] ?: ""
        }


    override suspend fun getCurrentLogin(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[LOGIN_KEY] ?: ""
        }

    override suspend fun getCurrentEmail(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[EMAIL_KEY] ?: ""
        }

    override suspend fun getCurrentUsername(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}