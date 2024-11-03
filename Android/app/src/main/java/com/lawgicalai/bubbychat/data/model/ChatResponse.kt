package com.lawgicalai.bubbychat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    val content: String,
)
