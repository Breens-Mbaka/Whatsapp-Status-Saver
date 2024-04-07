package com.breens.whatsappstatussaver.preferences.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.breens.whatsappstatussaver.preferences.domain.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) :
    PreferencesRepository {
    override suspend fun setUri(uri: Uri?) {
        Log.e("PreferenceRepository", "setUri: $uri")
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.URI] = uri.toString()
        }
    }

    override suspend fun getUri(): Flow<Uri?> {
        return dataStore.data.map { preferences ->
            val uriString = preferences[PreferencesKeys.URI]
            Log.e("PreferenceRepository", "getUri: ${uriString?.toUri()}")
            uriString?.toUri()
        }
    }

    override suspend fun setIsOnboardingCompleted(isOnBoardingCompleted: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.IS_ONBOARDED_COMPLETED] = isOnBoardingCompleted
        }
    }

    override suspend fun isOnBoardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDED_COMPLETED] ?: false
        }
    }
}

object PreferencesKeys {
    val URI = stringPreferencesKey("uri")

    val IS_ONBOARDED_COMPLETED = booleanPreferencesKey("is_onboarded")
}