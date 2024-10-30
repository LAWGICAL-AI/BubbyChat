package com.lawgicalai.bubbychat.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetChatResponseStreamUseCase {
    suspend operator fun invoke(input: String): Result<Flow<String>>
}