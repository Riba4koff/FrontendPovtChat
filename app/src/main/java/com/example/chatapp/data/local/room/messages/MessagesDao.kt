package com.example.chatapp.data.local.room.messages

import androidx.room.*
import com.example.chatapp.data.local.room.entity.MessageEntity

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    suspend fun fetchAllMessages(): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)
}