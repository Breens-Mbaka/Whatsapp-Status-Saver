package com.breens.whatsappstatussaver.preferences.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun setUri(uri: Uri?)

    suspend fun getUri(): Flow<Uri?>

    suspend fun setIsOnboardingCompleted(isOnBoardingCompleted: Boolean)

    suspend fun isOnBoardingCompleted(): Flow<Boolean>
}
