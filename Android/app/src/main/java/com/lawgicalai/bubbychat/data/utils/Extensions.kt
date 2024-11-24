package com.lawgicalai.bubbychat.data.utils

import okio.BufferedSource
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.formatToKoreanDate(): String {
    val dateTime = LocalDateTime.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    return dateTime.format(formatter)
}

suspend fun BufferedSource.processEventStream(onData: suspend (String) -> Unit) {
    use { source ->
        while (!source.exhausted()) {
            val line = source.readUtf8LineStrict()
            if (line.startsWith("data:")) {
                val dataContent = line.removePrefix("data:").trim()
                Timber.tag("Streaming").d("데이터 수신: $dataContent")
                onData(dataContent)
            }
        }
    }
}
