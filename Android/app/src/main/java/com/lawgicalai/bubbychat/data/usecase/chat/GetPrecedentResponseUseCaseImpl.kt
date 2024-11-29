package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.api.ChatApi
import com.lawgicalai.bubbychat.data.di.utils.ApiResult
import com.lawgicalai.bubbychat.data.di.utils.safeApiCall
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.domain.model.PrecedentBody
import com.lawgicalai.bubbychat.domain.usecase.GetPrecedentResponseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GetPrecedentResponseUseCaseImpl
    @Inject
    constructor(
        private val chatApi: ChatApi,
    ) : GetPrecedentResponseUseCase {
        override suspend fun invoke(input: String): Flow<Result<PrecedentBody>> =
            flow {
                when (val result = safeApiCall { chatApi.fetchPrecedent(CommonRequest(input)) }) {
                    is ApiResult.Error -> {
                        emit(Result.failure(result.exception))
                        Timber.tag("PrecedentBody").e(result.exception, "Error fetching stream response")
                    }

                    is ApiResult.Success -> {
                        result.data.output?.let { data ->
                            emit(Result.success(data))
                        }
                    }
                }
            }
    }
