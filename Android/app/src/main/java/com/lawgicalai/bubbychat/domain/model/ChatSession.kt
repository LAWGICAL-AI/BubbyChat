package com.lawgicalai.bubbychat.domain.model

import java.time.LocalDateTime

data class ChatSession(
    val text: String,
    val timestamp: LocalDateTime,
    val sessionId: Int,
    val firstResponse: String,
)
