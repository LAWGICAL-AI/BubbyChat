package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.ChatMessage

interface SaveChatMessagesUseCase {
    suspend operator fun invoke(
        messages: List<ChatMessage>,
        email: String,
    )
}
