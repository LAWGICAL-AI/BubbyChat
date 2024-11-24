package com.lawgicalai.bubbychat.domain.usecase

interface SignOutUseCase {
    suspend operator fun invoke(): Result<Unit>
}
