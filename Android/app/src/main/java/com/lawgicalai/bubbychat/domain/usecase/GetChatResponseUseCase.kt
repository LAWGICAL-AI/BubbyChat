package com.lawgicalai.bubbychat.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetChatResponseUseCase {
    suspend operator fun invoke(input: String): Flow<Result<String>>
}
