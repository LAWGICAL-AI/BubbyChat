package com.lawgicalai.bubbychat.data.deprecated

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
    fun provideUserService(httpClient: HttpClient): UserService = UserService(httpClient)

    @Provides
    @Singleton
    fun provideChatService(httpClient: HttpClient): ChatService = ChatService(httpClient)
}
