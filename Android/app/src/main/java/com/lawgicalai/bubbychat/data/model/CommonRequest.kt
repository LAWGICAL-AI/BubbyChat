package com.lawgicalai.bubbychat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonRequest(
    val input: String,
)
