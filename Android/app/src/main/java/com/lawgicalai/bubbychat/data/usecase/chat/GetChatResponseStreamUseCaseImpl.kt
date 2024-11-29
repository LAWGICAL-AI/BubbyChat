package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.BuildConfig
import com.lawgicalai.bubbychat.data.model.CommonRequest
import com.lawgicalai.bubbychat.data.utils.processEventStream
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "GetChatResponseStreamUs"

class GetChatResponseStreamUseCaseImpl
    @Inject
    constructor(
//        private val client: OkHttpClient, // 이게 한번에 로그를 묶어서 주려고 하면서, 딜레이가 걸린다
    ) : GetChatResponseStreamUseCase {
        private val client = OkHttpClient()

        override suspend fun invoke(input: String): Flow<Result<String>> =
            flow {
                val request =
                    Request
                        .Builder()
                        .url(BuildConfig.BASE_URL + "stream")
                        .header("Accept", "text/event-stream")
                        .post(CommonRequest(input).toRequestBody())
                        .build()
                Timber.tag(TAG).d("$request")
                val call = client.newCall(request)

                try {
                    val response = call.execute()
                    if (!response.isSuccessful) {
                        throw Exception("HTTP 에러코드: ${response.code}")
                    }
                    response.body?.source()?.processEventStream { dataContent ->
                        emit(Result.success(dataContent))
                    }
                } catch (e: Exception) {
                    Timber.tag("Streaming").e(e, "Streaming 에러 발생")
                    emit(Result.failure(e))
                    return@flow
                }
            }.flowOn(Dispatchers.IO) // Okhttp는 IO 스레드에서 실행되어야 함
    }
