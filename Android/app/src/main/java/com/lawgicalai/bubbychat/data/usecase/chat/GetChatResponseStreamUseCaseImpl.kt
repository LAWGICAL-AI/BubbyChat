package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.BuildConfig
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

class GetChatResponseStreamUseCaseImpl
    @Inject
    constructor() : GetChatResponseStreamUseCase {
        private val client = OkHttpClient()

        override suspend fun invoke(input: String): Flow<Result<String>> =
            flow {
                val request =
                    Request
                        .Builder()
                        .url(BuildConfig.BASE_URL + "stream")
                        .header("Accept", "text/event-stream")
                        .post(
                            "{\"input\":\"$input\"}".toRequestBody("application/json".toMediaTypeOrNull()),
                        ).build()

                val call = client.newCall(request)

                try {
                    val response = call.execute()
                    if (!response.isSuccessful) {
                        emit(Result.failure(Exception("HTTP error code: ${response.code}")))
                        return@flow
                    }

                    response.body?.source()?.use { source ->
                        Timber.tag("Streaming").d("Received data: $source")
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
                    Timber.tag("Streaming").e(e, "Streaming 에러 발생")
                    emit(Result.failure(e))
                    return@flow
                }

//                val request =
//                    Request
//                        .Builder()
//                        .url(BuildConfig.BASE_URL + "stream")
//                        .header("Accept", "text/event-stream")
//                        .post(CommonRequest(input).toRequestBody())
//                        .build()
//
//                val call = client.newCall(request)
//
//                try {
//                    val response = call.execute()
//                    if (!response.isSuccessful) {
//                        throw Exception("HTTP 에러코드: ${response.code}")
//                    }
//                    response.body?.source()?.processEventStream { dataContent ->
//                        Timber.tag("Streaming").d("데이터 수신: $dataContent")
//                        emit(Result.success(dataContent))
//                    }
//                } catch (e: Exception) {
//                    Timber.tag("Streaming").e(e, "Streaming 에러 발생")
//                    emit(Result.failure(e))
//                    return@flow
//                }
            }.flowOn(Dispatchers.IO) // Okhttp는 IO 스레드에서 실행되어야 함
    }
