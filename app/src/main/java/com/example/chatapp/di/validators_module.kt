package com.example.chatapp.di

import com.example.chatapp.presentation.validators.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val validators_module = module {
    singleOf(::EmailValidator){
        bind<IEmailValidator>()
    }
    singleOf(::LoginValidator){
        bind<ILoginValidator>()
    }
    singleOf(::PasswordValidator){
        bind<IPasswordValidator>()
    }
    singleOf(::CredentialValidator){
        bind<ICredentialValidator>()
    }
}