package com.lawgicalai.bubbychat.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CommonRequest(
    val input: String,
) {
    fun toRequestBody(): RequestBody =
        Json
            .encodeToString(this)
            .toRequestBody("application/json".toMediaTypeOrNull())
}
