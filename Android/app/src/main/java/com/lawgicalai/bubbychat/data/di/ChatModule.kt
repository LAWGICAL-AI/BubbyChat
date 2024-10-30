package com.lawgicalai.bubbychat.data.di

import com.lawgicalai.bubbychat.data.usecase.chat.GetChatResponseStreamUseCaseImpl
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    abstract fun bindGetChatResponseStreamUseCase(
        uc: GetChatResponseStreamUseCaseImpl
    ): GetChatResponseStreamUseCase
}