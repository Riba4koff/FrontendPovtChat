package com.example.chatapp.di

import com.example.chatapp.data.remote.ChatSocketService.ChatSocketService
import com.example.chatapp.data.remote.ChatSocketService.IChatSocketService
import com.example.chatapp.data.remote.KtorClient.AuthApi
import com.example.chatapp.data.remote.KtorClient.IAuthApi
import com.example.chatapp.data.remote.KtorClient.KtorHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val api_module = module {
    singleOf(::AuthApi){
        bind<IAuthApi>()
    }
    singleOf(::KtorHttpClient)
    singleOf(::ChatSocketService){
        bind<IChatSocketService>()
    }
}