package com.lawgicalai.bubbychat.data.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import com.lawgicalai.bubbychat.data.repository.AuthRepository
import com.lawgicalai.bubbychat.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {
    @Provides
    @ActivityScoped
    fun provideLoginRepository(
        @ActivityContext context: Context,
        auth: FirebaseAuth,
        credentialManager: CredentialManager,
        dataStore: UserDataStore,
    ): AuthRepository = AuthRepositoryImpl(context, auth, credentialManager, dataStore = dataStore)
}
