package com.example.chatapp

import android.app.Application
import com.example.chatapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ChatApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ChatApp)
            modules(
                validators_module,
                viewmodel_module,
                preferences_module,
                repository_module,
                session_manager_module,
                api_module,
                data_module,
                use_cases_module
            )
        }
    }
}