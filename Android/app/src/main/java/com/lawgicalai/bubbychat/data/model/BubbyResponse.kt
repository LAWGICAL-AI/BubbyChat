package com.lawgicalai.bubbychat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BubbyResponse<T>(
    @SerialName("isQuestion")
    val isQuestion: Boolean,
    @SerialName("output")
    val output: T?,
    @SerialName("isEOF")
    val eof: Boolean,
)
