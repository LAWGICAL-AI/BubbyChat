package com.lawgicalai.bubbychat.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "chat_messages",
    foreignKeys = [
        ForeignKey( // FK로 세션 테이블을 참조하도록 설정 -> 세션 삭제 시 메시지도 날아가게
            entity = ChatSessionEntity::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val text: String,
    val isMine: Boolean,
    val timestamp: LocalDateTime,
    val sessionId: Int, // 각 메시지를 세션과 연결하는 필드
)
