package com.lawgicalai.bubbychat.domain.model

data class ChatSession(
    val text: String,
    val timestamp: String,
    val sessionId: Int,
    val firstResponse: String,
)
