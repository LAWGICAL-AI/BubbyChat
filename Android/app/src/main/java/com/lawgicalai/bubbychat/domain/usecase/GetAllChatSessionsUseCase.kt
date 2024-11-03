package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.ChatSession
import kotlinx.coroutines.flow.Flow

interface GetAllChatSessionsUseCase {
    suspend operator fun invoke(): Flow<List<ChatSession>>
}
