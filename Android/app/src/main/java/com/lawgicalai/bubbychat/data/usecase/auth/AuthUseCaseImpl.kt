package com.lawgicalai.bubbychat.data.usecase.auth

import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import com.lawgicalai.bubbychat.data.repository.AuthRepository
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import com.lawgicalai.bubbychat.domain.usecase.SignInWithGoogleUseCase
import com.lawgicalai.bubbychat.domain.usecase.SignOutUseCase
import javax.inject.Inject

class GetCurrentUserUseCaseImpl
    @Inject
    constructor(
        private val dataStore: UserDataStore,
    ) : GetCurrentUserUseCase {
        override suspend fun invoke() = dataStore.getUser()
    }

class SignInWithGoogleUseCaseImpl
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : SignInWithGoogleUseCase {
        override suspend fun invoke(): Result<User> = authRepository.signInWithGoogle()
    }

class SignOutUseCaseImpl
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : SignOutUseCase {
        override suspend fun invoke(): Result<Unit> = authRepository.signOut()
    }
