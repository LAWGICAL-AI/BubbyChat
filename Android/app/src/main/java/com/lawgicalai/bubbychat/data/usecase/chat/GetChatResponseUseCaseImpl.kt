package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.api.ChatApi
import com.lawgicalai.bubbychat.data.di.utils.ApiResult
import com.lawgicalai.bubbychat.data.di.utils.safeApiCall
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetChatResponseUseCaseImpl
    @Inject
    constructor(
        private val chatApi: ChatApi,
    ) : GetChatResponseUseCase {
        override suspend fun invoke(input: String): Flow<Result<String>> =
            flow {
                when (val result = safeApiCall { chatApi.fetchChatResponse(CommonRequest(input)) }) {
                    is ApiResult.Error -> {
                        emit(Result.failure(result.exception))
                        Timber.tag("Streaming").e(result.exception, "Error fetching stream response")
                    }

                    is ApiResult.Success -> {
                        result.data.output?.let {
                            emit(Result.success(it))
                        }
                    }
                }
            }
    }
