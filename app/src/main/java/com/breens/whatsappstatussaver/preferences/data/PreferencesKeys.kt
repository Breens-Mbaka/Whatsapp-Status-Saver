package com.breens.whatsappstatussaver.preferences.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val PERMISSION_GRANTED = booleanPreferencesKey("permission_granted")

    val URI = stringPreferencesKey("uri")

    val IS_ONBOARDED_COMPLETED = booleanPreferencesKey("is_onboarded")
}
