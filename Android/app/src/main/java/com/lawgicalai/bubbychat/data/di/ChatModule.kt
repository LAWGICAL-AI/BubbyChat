package com.lawgicalai.bubbychat.data.di

import com.lawgicalai.bubbychat.data.usecase.chat.GetAllChatSessionsUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.chat.GetChatResponseStreamUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.chat.GetChatResponseUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.chat.GetChatSessionUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.chat.GetPrecedentResponseUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.chat.SaveChatMessagesUseCaseImpl
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetChatSessionUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetPrecedentResponseUseCase
import com.lawgicalai.bubbychat.domain.usecase.SaveChatMessagesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    abstract fun bindGetChatResponseStreamUseCase(uc: GetChatResponseStreamUseCaseImpl): GetChatResponseStreamUseCase

    @Binds
    abstract fun bindGetChatResponseUseCase(uc: GetChatResponseUseCaseImpl): GetChatResponseUseCase

    @Binds
    abstract fun bindSaveChatMessagesUseCase(uc: SaveChatMessagesUseCaseImpl): SaveChatMessagesUseCase

    @Binds
    abstract fun bindGetChatSessionUseCase(uc: GetChatSessionUseCaseImpl): GetChatSessionUseCase

    @Binds
    abstract fun bindGetAllChatSessionsUseCase(uc: GetAllChatSessionsUseCaseImpl): GetAllChatSessionsUseCase

    @Binds
    abstract fun bindGetPrecedentResponseUseCase(uc: GetPrecedentResponseUseCaseImpl): GetPrecedentResponseUseCase
}
