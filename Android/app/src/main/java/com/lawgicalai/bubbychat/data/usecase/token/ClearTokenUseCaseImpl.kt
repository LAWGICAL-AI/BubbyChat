package com.lawgicalai.bubbychat.data.usecase.token

import com.lawgicalai.bubbychat.domain.usecase.ClearTokenUseCase
import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import javax.inject.Inject


class ClearTokenUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore,
) : ClearTokenUseCase {
    override suspend fun invoke(): Result<Unit> = runCatching {
        userDataStore.clear()
    }
}