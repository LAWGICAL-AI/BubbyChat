package com.lawgicalai.bubbychat.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.lawgicalai.bubbychat.BuildConfig
import com.lawgicalai.bubbychat.data.di.utils.isJsonArray
import com.lawgicalai.bubbychat.data.di.utils.isJsonObject
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
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor {
                when {
                    !it.isJsonArray() && !it.isJsonObject() ->
                        Timber.tag("RETROFIT").d("CONNECTION INFO: $it")

                    else ->
                        try {
                            Timber.tag("RETROFIT").d(
                                GsonBuilder().setPrettyPrinting().create().toJson(
                                    JsonParser().parse(it),
                                ),
                            )
                        } catch (m: JsonSyntaxException) {
                            Timber.tag("RETROFIT").d(it)
                        }
                }
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    const val NETWORK_TIMEOUT = 10L
}
