package com.lawgicalai.bubbychat.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Int = 0,
    val email: String,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val title: String = "Chat Session",
    val firstResponse: String = "Chat Session",
)
