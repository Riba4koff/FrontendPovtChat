package com.example.chatapp.data.local.room.messages

import androidx.room.*
import com.example.chatapp.data.local.room.entity.MessageEntity
import java.util.concurrent.Flow

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    suspend fun fetchAllMessages(): List<MessageEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    @Update
    suspend fun updateMessage(message: MessageEntity)
    @Query("SELECT * FROM messages WHERE id_message=:id")
    suspend fun fetchMessageById(id: Long): MessageEntity

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}