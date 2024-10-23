package com.lawgicalai.bubbychat.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "user_token")

class UserDataStore @Inject constructor(
    private val context: Context,
) {
    suspend fun setToken(token: String) {
        context.dataStore.edit { pref ->
            pref[KEY_TOKEN] = token
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { pref ->
            pref[KEY_TOKEN]
        }.first()
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("token")
    }
}