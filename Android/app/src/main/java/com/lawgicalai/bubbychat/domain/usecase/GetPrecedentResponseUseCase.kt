package com.lawgicalai.bubbychat.domain.usecase

import com.lawgicalai.bubbychat.domain.model.PrecedentBody
import kotlinx.coroutines.flow.Flow

interface GetPrecedentResponseUseCase {
    suspend operator fun invoke(input: String): Flow<Result<PrecedentBody>>
}
