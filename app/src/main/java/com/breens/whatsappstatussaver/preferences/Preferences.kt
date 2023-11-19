package com.breens.whatsappstatussaver.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.breens.whatsappstatussaver.constants.WHATSAPP_STATUS_SAVER_PREFERENCES

object Preferences {
    val Context.dataStore by preferencesDataStore(
        name = WHATSAPP_STATUS_SAVER_PREFERENCES,
    )
}
