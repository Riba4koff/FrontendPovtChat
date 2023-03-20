package com.example.chatapp.di

import com.example.chatapp.data.preferencesDataStore.SessionManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val session_manager_module = module {
    single {
        SessionManager(get())
    }
}