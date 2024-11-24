package com.lawgicalai.bubbychat.data.repository

import com.lawgicalai.bubbychat.domain.model.User

interface AuthRepository {
    suspend fun getCurrentUser(): User?

    suspend fun signInWithGoogle(): Result<User>

    suspend fun signOut(): Result<Unit>
}
