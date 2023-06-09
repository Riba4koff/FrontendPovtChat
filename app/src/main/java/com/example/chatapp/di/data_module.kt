package com.example.chatapp.di

import androidx.room.Room
import com.example.chatapp.data.local.room.DataStores.IMessagesDataStore
import com.example.chatapp.data.local.room.DataStores.MessagesDataStore
import com.example.chatapp.data.local.room.DataBases.MessagesDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val data_module = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MessagesDataBase::class.java,
            MessagesDataBase.DB_NAME
        ).fallbackToDestructiveMigration().build().messagesDao()
    }

    singleOf(::MessagesDataStore){
        bind<IMessagesDataStore>()
    }
}