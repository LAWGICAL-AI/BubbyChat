package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetCurrentUserUseCase {
    suspend operator fun invoke(): Flow<User?>
}
