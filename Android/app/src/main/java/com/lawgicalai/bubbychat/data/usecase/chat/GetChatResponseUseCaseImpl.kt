package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.api.ChatApi
import com.lawgicalai.bubbychat.data.di.utils.ApiResult
import com.lawgicalai.bubbychat.data.di.utils.safeApiCall
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.domain.model.ChatChunk
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetChatResponseUseCaseImpl
    @Inject
    constructor(
        private val chatApi: ChatApi,
    ) : GetChatResponseUseCase {
        override suspend fun invoke(input: String): Flow<Result<ChatChunk>> =
            flow {
                when (val result = safeApiCall { chatApi.fetchChatResponse(CommonRequest(input)) }) {
                    is ApiResult.Error -> {
                        emit(Result.failure(result.exception))
                        Timber.tag("Streaming").e(result.exception, "Error fetching stream response")
                    }

                    is ApiResult.Success -> {
                        result.data.output?.let { data ->
                            val fullText = data.content.replace(Regex("""\\u003c\|.*?\|\u003e"""), "")
                            try {
                                val chunkSize = 3 // 한 번에 emit할 글자 수
                                for (i in fullText.indices step chunkSize) {
                                    val chunk =
                                        fullText.substring(
                                            i,
                                            (i + chunkSize).coerceAtMost(fullText.length),
                                        )
                                    val isEnd = i + chunkSize >= fullText.length
                                    emit(Result.success(ChatChunk(chunk, isEnd)))
                                    delay(50) // 청크 간의 지연 시간
                                }
                            } catch (e: Exception) {
                                Timber.tag("Chat").e(e, "Error streaming response in chunks")
                                emit(Result.failure(e))
                            }
                        }
                    }
                }
            }
    }
