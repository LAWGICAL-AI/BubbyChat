package com.lawgicalai.bubbychat.data.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonLogger
    @Inject
    constructor(
        private val gson: Gson,
    ) {
        private fun formatJsonOrDefault(json: String): String =
            runCatching {
                JsonParser.parseString(json).let { jsonElement ->
                    gson.toJson(jsonElement)
                }
            }.getOrDefault(json)

        fun logJsonWithTag(
            json: String,
            tag: String = DEFAULT_TAG,
        ) {
            Timber.tag(tag).d(formatJsonOrDefault(json))
        }

        companion object {
            const val DEFAULT_TAG = "NETWORK"
        }
    }
