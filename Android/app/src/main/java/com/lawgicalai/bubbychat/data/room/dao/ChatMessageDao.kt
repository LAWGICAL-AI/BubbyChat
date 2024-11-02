package com.lawgicalai.bubbychat.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lawgicalai.bubbychat.data.room.entity.ChatMessageEntity
import com.lawgicalai.bubbychat.data.room.entity.ChatSessionEntity

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChatSessionEntity): Long

    // 특정 세션의 메시지 가져오기
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp DESC")
    suspend fun getMessagesForSession(sessionId: Int): List<ChatMessageEntity>

    // 모든 세션 가져오기
    @Query("SELECT * FROM chat_sessions ORDER BY startTime DESC")
    suspend fun getAllSessions(): List<ChatSessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<ChatMessageEntity>
}
