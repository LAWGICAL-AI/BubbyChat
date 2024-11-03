package com.lawgicalai.bubbychat.data.api

import com.lawgicalai.bubbychat.data.model.BubbyResponse
import com.lawgicalai.bubbychat.data.model.ChatResponse
import com.lawgicalai.bubbychat.data.model.CommonRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApi {
    @POST("stream")
    @Headers("Accept: text/event-stream")
    suspend fun fetchStreamResponse(
        @Body input: CommonRequest,
    ): Response<ResponseBody>

    @POST("chat")
    suspend fun fetchChatResponse(
        @Body input: CommonRequest,
    ): BubbyResponse<ChatResponse>
}
