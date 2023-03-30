package com.example.chatapp.data.local.room.DataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatapp.data.local.room.Dao.MessagesDao
import com.example.chatapp.data.local.room.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MessagesDataBase : RoomDatabase(){
    abstract fun messagesDao() : MessagesDao

    companion object {
        const val DB_NAME = "messages_db"
    }
}