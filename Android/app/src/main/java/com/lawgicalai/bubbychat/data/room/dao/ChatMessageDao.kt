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

    // 특정 이메일의 특정 세션 메시지 가져오기
    @Query("SELECT * FROM chat_messages WHERE email = :email AND sessionId = :sessionId ORDER BY timestamp")
    suspend fun getMessagesForSession(
        email: String,
        sessionId: Int,
    ): List<ChatMessageEntity>

    // 특정 이메일의 모든 세션 가져오기
    @Query("SELECT * FROM chat_sessions WHERE email = :email ORDER BY startTime DESC")
    suspend fun getAllSessions(email: String): List<ChatSessionEntity>

    // 특정 이메일의 모든 메시지 가져오기
    @Query("SELECT * FROM chat_messages WHERE email = :email ORDER BY timestamp DESC")
    suspend fun getAllMessages(email: String): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    // 특정 이메일의 가장 최근 세션 가져오기
    @Query("SELECT * FROM chat_sessions WHERE email = :email ORDER BY startTime DESC LIMIT 1")
    suspend fun getLatestSession(email: String): ChatSessionEntity?

    // 특정 이메일의 세션 삭제
    @Query("DELETE FROM chat_sessions WHERE email = :email AND sessionId = :sessionId")
    suspend fun deleteSession(
        email: String,
        sessionId: Int,
    )

    // 특정 이메일의 모든 데이터 삭제
    @Query("DELETE FROM chat_sessions WHERE email = :email")
    suspend fun deleteAllSessions(email: String)
}
