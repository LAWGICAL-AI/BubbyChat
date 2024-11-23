package com.lawgicalai.bubbychat.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lawgicalai.bubbychat.BuildConfig
import com.lawgicalai.bubbychat.data.di.utils.isJsonArray
import com.lawgicalai.bubbychat.data.di.utils.isJsonObject
import com.lawgicalai.bubbychat.data.utils.JsonLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(logger: HttpLoggingInterceptor) =
        OkHttpClient.Builder().run {
            addInterceptor(logger)
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            build()
        }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(jsonLogger: JsonLogger): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor { message ->
                when {
                    !message.isJsonArray() && !message.isJsonObject() ->
                        Timber.tag(JsonLogger.DEFAULT_TAG).d("CONNECTION INFO: $message")

                    else -> jsonLogger.logJsonWithTag(message)
                }
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    const val NETWORK_TIMEOUT = 20L
}
