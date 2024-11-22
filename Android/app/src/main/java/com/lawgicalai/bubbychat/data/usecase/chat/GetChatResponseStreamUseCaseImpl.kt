package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.BuildConfig
import com.lawgicalai.bubbychat.data.api.ChatApi
import com.lawgicalai.bubbychat.data.di.utils.ApiResult
import com.lawgicalai.bubbychat.data.di.utils.safeApiCall
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSource
import timber.log.Timber
import javax.inject.Inject

class GetChatResponseStreamUseCaseImpl
@Inject
constructor(
    private val chatApi: ChatApi,
) : GetChatResponseStreamUseCase {
    private val client = OkHttpClient()
    override suspend fun fetchStreamResponse(input: String): Flow<Result<String>> = flow {
        val request = Request.Builder()
            .url(BuildConfig.BASE_URL + "stream")
            .header("Accept", "text/event-stream")
            .post(
                "{\"input\":\"$input\"}".toRequestBody("application/json".toMediaTypeOrNull())
            )
            .build()

        val call = client.newCall(request)

        try {
            val response = call.execute()
            if (!response.isSuccessful) {
                emit(Result.failure(Exception("HTTP error code: ${response.code}")))
                return@flow
            }

            response.body?.source()?.use { source ->
                while (!source.exhausted()) {
                    val line = source.readUtf8LineStrict()
                    if (line.startsWith("data:")) {
                        val dataContent = line.removePrefix("data:").trim()
                        Timber.tag("Streaming").d("Received data: $dataContent")
                        emit(Result.success(dataContent))
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag("Streaming").e(e, "Error while streaming response")
            emit(Result.failure(e))
        }!!
    }.flowOn(Dispatchers.IO)

    override suspend fun invoke(input: String): Flow<Result<String>> =
        flow {
            when (val result = safeApiCall { chatApi.fetchStreamResponse(CommonRequest(input)) }) {
                is ApiResult.Error -> {
                    emit(Result.failure(result.exception))
                    Timber.tag("Streaming").e(result.exception, "Error fetching stream response")
                }

                is ApiResult.Success -> {
                    val source: BufferedSource = result.data.source().buffer
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
                        result.data.close()
                    }
                }
            }
        }
}