package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.User

interface SignInWithGoogleUseCase {
    suspend operator fun invoke(): Result<User>
}
