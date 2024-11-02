package com.lawgicalai.bubbychat.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChatMessage(
    val text: String,
    val isMine: Boolean,
)
