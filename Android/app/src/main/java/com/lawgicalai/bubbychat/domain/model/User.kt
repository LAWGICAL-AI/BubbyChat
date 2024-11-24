package com.lawgicalai.bubbychat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String?,
    val displayName: String?,
    val profileImage: String?,
)
