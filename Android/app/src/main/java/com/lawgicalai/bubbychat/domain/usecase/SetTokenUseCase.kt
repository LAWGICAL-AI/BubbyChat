package com.lawgicalai.bubbychat.domain.usecase

interface SetTokenUseCase {
    suspend operator fun invoke(token: String)
}