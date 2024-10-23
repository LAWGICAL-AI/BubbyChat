package com.lawgicalai.bubbychat.data.di

import android.util.Log
import com.lawgicalai.bubbychat.domain.usecase.GetTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Singleton

private const val TAG = "KtorModule"
@Module
@InstallIn(SingletonComponent::class)
object KtorModule {
    val BASE_URL = "10.0.2.2:8080/api" // localhost - emulator
    //    val BASE_URL = "192.168.0.21:8080/api" // localhost - 집
    private const val NETWORK_TIME_OUT = 6_000L // 6초

    @Provides
    @Singleton
    fun provideKtorClient(getTokenUseCase: GetTokenUseCase) = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = NETWORK_TIME_OUT
            connectTimeoutMillis = NETWORK_TIME_OUT
            socketTimeoutMillis = NETWORK_TIME_OUT
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            host = BASE_URL
        }

        // AccessTokenInterceptor 추가
       defaultRequest {
            val token = runBlocking { getTokenUseCase() }
            Log.d(TAG, "provideKtorClient: $token")
            token?.let {
                headers[HttpHeaders.Authorization] = "Bearer $it" // Bearer token
            }
        }
    }
}