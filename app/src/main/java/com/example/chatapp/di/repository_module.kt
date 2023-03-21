package com.example.chatapp.di

import com.example.chatapp.data.repository.MessagesRepository
import com.example.chatapp.data.repository.UserRepository
import com.example.chatapp.domain.irepository.IMessagesRepository
import com.example.chatapp.domain.irepository.IUserRepository
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