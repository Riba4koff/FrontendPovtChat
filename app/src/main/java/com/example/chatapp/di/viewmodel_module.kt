package com.example.chatapp.di

import com.example.chatapp.presentation.viewModel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewmodel_module = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditUserInfoViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::SplashViewViewModel)
}