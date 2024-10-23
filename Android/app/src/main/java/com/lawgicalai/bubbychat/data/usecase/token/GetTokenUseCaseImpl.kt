package com.lawgicalai.bubbychat.data.usecase.token

import com.lawgicalai.bubbychat.domain.usecase.GetTokenUseCase
import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import javax.inject.Inject


class GetTokenUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore,
) : GetTokenUseCase {
    override suspend fun invoke(): String? {
        return userDataStore.getToken()
    }
}