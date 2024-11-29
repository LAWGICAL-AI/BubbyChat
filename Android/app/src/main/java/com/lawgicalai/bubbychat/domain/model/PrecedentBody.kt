package com.lawgicalai.bubbychat.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrecedentBody(
    @SerialName("id")
    val id: String,
    @SerialName("precedent")
    val precedent: List<Precedent>,
) {
    @Serializable
    data class Precedent(
        @SerialName("case_name")
        val case_name: String,
        @SerialName("case_number")
        val case_number: String,
        @SerialName("case_type")
        val case_type: String,
        @SerialName("ref_article")
        val ref_article: String,
        @SerialName("url")
        val url: String,
    )
}
