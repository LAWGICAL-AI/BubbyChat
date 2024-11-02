package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.api.ChatApi
import com.lawgicalai.bubbychat.data.di.utils.ApiResult
import com.lawgicalai.bubbychat.data.di.utils.safeApiCall
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.BufferedSource
import timber.log.Timber
import javax.inject.Inject

class GetChatResponseStreamUseCaseImpl
    @Inject
    constructor(
        private val chatApi: ChatApi,
    ) : GetChatResponseStreamUseCase {
        override suspend fun invoke(input: String): Flow<Result<String>> =
            flow {
                when (val result = safeApiCall { chatApi.fetchStreamResponse(CommonRequest(input)) }) {
                    is ApiResult.Error -> {
                        emit(Result.failure(result.exception))
                        Timber.tag("Streaming").e(result.exception, "Error fetching stream response")
                    }

                    is ApiResult.Success -> {
                        result.data.body()?.let { response ->
                            val source: BufferedSource = response.source().buffer
                            try {
                                while (!source.exhausted()) {
                                    val line = source.readUtf8Line()
                                    if (line != null && line.startsWith("data:")) {
                                        val dataContent = line.removePrefix("data:").trim()
                                        Timber.tag("Streaming").d("Received data: $dataContent")
                                        emit(Result.success(dataContent)) // 실시간으로 각 청크를 emit하여 뷰모델에 전달
                                        delay(50)
                                    }
                                }
                            } catch (e: Exception) {
                                Timber.tag("Streaming").e(e, "Error reading stream")
                            } finally {
                                response.close()
                            }
                        }
                    }
                }
            }
    }
