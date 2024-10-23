package com.lawgicalai.bubbychat.data.di

import com.lawgicalai.bubbychat.data.usecase.token.ClearTokenUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.token.GetTokenUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.token.SetTokenUseCaseImpl
import com.lawgicalai.bubbychat.domain.usecase.ClearTokenUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetTokenUseCase
import com.lawgicalai.bubbychat.domain.usecase.SetTokenUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindGetTokenUseCase(
        uc: GetTokenUseCaseImpl,
    ): GetTokenUseCase

    @Binds
    abstract fun bindSetTokenUseCase(
        uc: SetTokenUseCaseImpl,
    ): SetTokenUseCase

    @Binds
    abstract fun bindClearTokenUseCase(
        uc: ClearTokenUseCaseImpl,
    ): ClearTokenUseCase
}