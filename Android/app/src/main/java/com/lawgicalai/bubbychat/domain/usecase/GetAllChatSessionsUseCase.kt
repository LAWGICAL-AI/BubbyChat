package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.data.room.entity.ChatSessionEntity

interface GetAllChatSessionsUseCase {
    suspend operator fun invoke(): List<ChatSessionEntity>
}
