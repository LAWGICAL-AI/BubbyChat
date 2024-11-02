package com.lawgicalai.bubbychat.data.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.formatToKoreanDate(): String {
    val dateTime = LocalDateTime.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    return dateTime.format(formatter)
}
