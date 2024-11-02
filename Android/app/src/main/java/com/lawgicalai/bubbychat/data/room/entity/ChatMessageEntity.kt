package com.lawgicalai.bubbychat.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isMine: Boolean,
    val timestamp: LocalDateTime,
    val sessionId: Int, // 각 메시지를 세션과 연결하는 필드
)
