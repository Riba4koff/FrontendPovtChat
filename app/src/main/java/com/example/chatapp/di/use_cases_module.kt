package com.example.chatapp.di

import com.example.chatapp.domain.UseCase.ConnectUseCase
import com.example.chatapp.domain.UseCase.GetAllMessagesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val use_cases_module = module {
    singleOf(::ConnectUseCase)
    singleOf(::GetAllMessagesUseCase)
}