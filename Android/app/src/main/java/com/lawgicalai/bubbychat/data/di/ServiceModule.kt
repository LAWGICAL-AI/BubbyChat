package com.lawgicalai.bubbychat.data.di

import com.lawgicalai.bubbychat.data.network.ChatService
import com.lawgicalai.bubbychat.data.network.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideUserService(httpClient: HttpClient): UserService {
        return UserService(httpClient)
    }

    @Provides
    @Singleton
    fun provideChatService(httpClient: HttpClient): ChatService {
        return ChatService(httpClient)
    }
}