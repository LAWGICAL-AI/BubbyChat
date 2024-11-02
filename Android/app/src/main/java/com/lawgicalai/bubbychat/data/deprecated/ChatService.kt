package com.lawgicalai.bubbychat.data.deprecated

import android.util.Log
import com.lawgicalai.bubbychat.data.model.CommonRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "ChatService"

class ChatService
    @Inject
    constructor(
        private val client: HttpClient,
    ) {
        fun startStreaming(question: String): Flow<String> =
            flow {
                try {
                    Log.d(TAG, "startStreaming: input: $question")
                    val channel: ByteReadChannel =
                        client
                            .post("stream") {
                                setBody(CommonRequest(input = question))
                            }.bodyAsChannel()

                    // 채널을 통한 데이터 수신 및 처리
                    while (!channel.isClosedForRead) {
                        val chunk = channel.readUTF8Line() // 한 줄씩 읽기
                        if (chunk != null && chunk.startsWith("data:")) { // "data:"로 시작하는 줄만 처리
                            val dataContent = chunk.removePrefix("data:").trim() // "data:" 제거 후 공백 제거
                            Log.d(TAG, "startStreaming: Received data - $dataContent")
                            emit(dataContent)
                            delay(50)
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "startStreaming: $e")
                    e.printStackTrace()
                } finally {
                    client.close()
                }
            }
    }
