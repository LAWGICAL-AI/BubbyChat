package com.lawgicalai.bubbychat.domain.usecase

interface GetTokenUseCase {
    suspend operator fun invoke(): String?
}