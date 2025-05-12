package com.komzak.wedriveevaluationassignment.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreHelper(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "user_prefs")
    private val phoneNumberKey = stringPreferencesKey("phone_number")
    private val accessTokenKey = stringPreferencesKey("access_token")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[accessTokenKey] = token
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[accessTokenKey]
        }
    }

    suspend fun savePhoneNumber(phoneNumber: String) {
        context.dataStore.edit { prefs ->
            prefs[phoneNumberKey] = phoneNumber
        }
    }

    fun getPhoneNumber(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[phoneNumberKey]
        }
    }

    suspend fun clearCache() {
        context.dataStore.edit { prefs ->
            prefs.remove(phoneNumberKey)
            prefs.remove(accessTokenKey)
        }
    }
}