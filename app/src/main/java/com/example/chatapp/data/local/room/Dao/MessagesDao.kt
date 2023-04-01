package com.example.chatapp.data.local.room.Dao

import androidx.room.*
import com.example.chatapp.data.local.room.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    fun fetchAllMessages(): Flow<List<MessageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)
    @Update
    suspend fun updateMessage(message: MessageEntity)
    @Query("SELECT * FROM messages WHERE id_message=:id")
    suspend fun fetchMessageById(id: Long): MessageEntity

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}