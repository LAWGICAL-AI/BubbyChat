package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.ChatChunk
import kotlinx.coroutines.flow.Flow

interface GetChatResponseUseCase {
    suspend operator fun invoke(input: String): Flow<Result<ChatChunk>>
}
