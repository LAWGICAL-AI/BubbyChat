package com.lawgicalai.bubbychat.data.network

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatApi {
    @POST("stream")
    @Headers("Accept: text/event-stream")
    suspend fun fetchStreamResponse(
        @Body input: String
    ): Result<ResponseBody>
}