package com.example.chatapp.di

import com.example.chatapp.data.repositoryImpl.MessagesRepository
import com.example.chatapp.data.repositoryImpl.UserRepository
import com.example.chatapp.domain.repository.IMessagesRepository
import com.example.chatapp.domain.repository.IUserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repository_module = module {
    singleOf(::UserRepository){
        bind<IUserRepository>()
    }
    singleOf(::MessagesRepository) {
        bind<IMessagesRepository>()
    }
}