package com.lawgicalai.bubbychat.data.usecase.token

import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import com.lawgicalai.bubbychat.domain.usecase.SetTokenUseCase
import javax.inject.Inject

class SetTokenUseCaseImpl @Inject constructor(
    private val userDataStore: UserDataStore
): SetTokenUseCase {
    override suspend fun invoke(token: String) {
        userDataStore.setToken(token)
    }
}