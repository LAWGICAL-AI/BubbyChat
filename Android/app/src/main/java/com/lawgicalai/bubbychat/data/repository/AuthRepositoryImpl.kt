package com.lawgicalai.bubbychat.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.data.datastore.UserDataStore
import com.lawgicalai.bubbychat.data.model.toUser
import com.lawgicalai.bubbychat.domain.model.User
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl
    @Inject
    constructor(
        @ActivityContext private val context: Context,
        private val auth: FirebaseAuth,
        private val credentialManager: CredentialManager,
        private val dataStore: UserDataStore,
    ) : AuthRepository {
        private val googleIdOption =
            GetGoogleIdOption
                .Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()

        private val credentialRequest =
            GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build()

        override suspend fun getCurrentUser(): User? = auth.currentUser?.toUser()

        override suspend fun signInWithGoogle(): Result<User> =
            try {
                Timber.tag(TAG).d("googleSignIn: start")
                // 먼저 Google One-tap으로부터 credential 획득
                val credential =
                    credentialManager.getCredential(
                        request = credentialRequest,
                        context = context,
                    )

                // credential로부터 idToken 추출
                when (val googleCredential = credential.credential) {
                    is CustomCredential -> {
                        if (googleCredential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            Timber.tag(TAG).d("googleSignIn: success")
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(googleCredential.data)

                            // idToken으로 Firebase 인증
                            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                            val authResult =
                                withContext(Dispatchers.IO) {
                                    auth.signInWithCredential(firebaseCredential).await()
                                }

                            authResult.user?.let {
                                dataStore.setUser(it.toUser())
                                Result.success(it.toUser())
                            } ?: Result.failure(Exception("Authentication failed"))
                        } else {
                            Timber.tag(TAG).d("googleSignIn: fail # 2")
                            Result.failure(Exception("Invalid credential type"))
                        }
                    }
                    else -> Result.failure(Exception("Invalid credential"))
                }
            } catch (e: Exception) {
                Timber.tag(TAG).d("googleSignIn: fail #1 $e")
                Result.failure(e)
            }

        override suspend fun signOut(): Result<Unit> =
            try {
                Timber.tag(TAG).d("signOut: start")
                credentialManager.clearCredentialState(
                    request = ClearCredentialStateRequest(),
                )
                auth.signOut()
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.tag(TAG).d("signOut: fail $e")
                Result.failure(e)
            }
    }
