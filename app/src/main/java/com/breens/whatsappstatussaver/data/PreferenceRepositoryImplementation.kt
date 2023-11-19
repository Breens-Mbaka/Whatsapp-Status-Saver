package com.breens.whatsappstatussaver.data

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.breens.whatsappstatussaver.preferences.Preferences.dataStore
import com.breens.whatsappstatussaver.preferences.PreferencesKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferenceRepositoryImplementation(@ApplicationContext private val context: Context) :
    PreferencesRepository {
    override fun getIsPermissionGranted(): Flow<Boolean> {
        val isPermissionGranted: Flow<Boolean> = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.PERMISSION_GRANTED] ?: false
            }
        return isPermissionGranted
    }

    override suspend fun setIsPermissionGranted(permissionGranted: Boolean) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.PERMISSION_GRANTED] = permissionGranted
        }
    }

    override suspend fun setUri(uri: Uri) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.URI] = uri.toString()
        }
    }

    override suspend fun getUri(): Flow<Uri> {
        val uri: Flow<Uri> = context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val uriString = preferences[PreferencesKeys.URI] ?: ""

            Uri.parse(uriString)
        }

        return uri
    }

    override suspend fun setIsOnboardingCompleted(isOnBoardingCompleted: Boolean) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferencesKeys.IS_ONBOARDED_COMPLETED] = isOnBoardingCompleted
        }
    }

    override suspend fun isOnBoardingCompleted(): Flow<Boolean> {
        val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDED_COMPLETED] ?: false
        }

        return isOnboardingCompleted
    }
}
