package com.lawgicalai.bubbychat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse<T>(
    val result: String,
    val data: T?, // 공통 응답 데이터
    val errorCode: String,
    val errorMessage: String,
)
