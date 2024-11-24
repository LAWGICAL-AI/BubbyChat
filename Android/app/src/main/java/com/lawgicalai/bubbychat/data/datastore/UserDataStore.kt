package com.lawgicalai.bubbychat.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lawgicalai.bubbychat.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_token")

private const val TAG = "UserDataStore"

class UserDataStore
    @Inject
    constructor(
        @ApplicationContext
        private val context: Context,
    ) {
        suspend fun setToken(token: String) {
            context.dataStore.edit { pref ->
                pref[KEY_TOKEN] = token
            }
        }

        suspend fun getToken(): String? =
            context.dataStore.data
                .map { pref ->
                    pref[KEY_TOKEN]
                }.first()

        suspend fun setUser(user: User) {
            Timber.tag(TAG).d("setUser: $user")
            context.dataStore.edit { pref ->
                user.displayName?.let { pref[KEY_USER_NAME] = it }
                user.email?.let { pref[KEY_USER_EMAIL] = it }
                user.profileImage?.let { pref[KEY_USER_PHOTO_URL] = it }
            }
        }

        fun getUser(): Flow<User?> =
            context.dataStore.data.map { pref ->
                val name = pref[KEY_USER_NAME] ?: return@map null
                Timber.tag(TAG).d("$pref")
                User(
                    displayName = name,
                    email = pref[KEY_USER_EMAIL],
                    profileImage = pref[KEY_USER_PHOTO_URL],
                )
            }

        suspend fun clear() {
            context.dataStore.edit {
                it.clear()
            }
        }

        companion object {
            private val KEY_TOKEN = stringPreferencesKey("token")
            private val KEY_USER_NAME = stringPreferencesKey("user_name")
            private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
            private val KEY_USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
        }
    }
