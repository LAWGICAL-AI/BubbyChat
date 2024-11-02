package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.ChatSession

interface GetAllChatSessionsUseCase {
    suspend operator fun invoke(): List<ChatSession>
}
