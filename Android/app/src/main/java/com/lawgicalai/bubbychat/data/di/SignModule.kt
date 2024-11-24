package com.lawgicalai.bubbychat.data.di

import com.lawgicalai.bubbychat.data.usecase.auth.SignInWithGoogleUseCaseImpl
import com.lawgicalai.bubbychat.data.usecase.auth.SignOutUseCaseImpl
import com.lawgicalai.bubbychat.domain.usecase.SignInWithGoogleUseCase
import com.lawgicalai.bubbychat.domain.usecase.SignOutUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class SignModule {
    @Binds
    @ActivityScoped
    abstract fun bindSignInWithGoogleUseCase(signInWithGoogleUseCaseImpl: SignInWithGoogleUseCaseImpl): SignInWithGoogleUseCase

    @Binds
    @ActivityScoped
    abstract fun bindSignOutUseCase(signOutUseCaseImpl: SignOutUseCaseImpl): SignOutUseCase
}
