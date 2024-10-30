package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.network.ChatService
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatResponseStreamUseCaseImpl @Inject constructor(
    private val chatService: ChatService
) : GetChatResponseStreamUseCase {
    override suspend fun invoke(input: String): Result<Flow<String>> {
        return try {
            Result.success(chatService.startStreaming(input)) // Flow를 직접 반환
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}