package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.ChatMessage

interface GetChatSessionUseCase {
    suspend operator fun invoke(
        sessionId: Int,
        email: String,
    ): List<ChatMessage>
}
