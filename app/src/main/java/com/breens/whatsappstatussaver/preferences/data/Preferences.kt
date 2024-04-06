package com.breens.whatsappstatussaver.preferences.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object Preferences {
    val Context.dataStore by preferencesDataStore(
        name =  "WHATSAPP_STATUS_SAVER_PREFERENCES",
    )
}