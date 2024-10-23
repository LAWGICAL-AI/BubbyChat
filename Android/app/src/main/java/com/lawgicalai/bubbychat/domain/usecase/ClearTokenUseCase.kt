package com.lawgicalai.bubbychat.domain.usecase

interface ClearTokenUseCase {
    suspend operator fun invoke(): Result<Unit>
}