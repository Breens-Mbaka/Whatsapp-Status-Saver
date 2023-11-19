package com.breens.whatsappstatussaver.common

import android.os.Build

fun saveImageToDownloads(
    saveImageForScopedStorage: () -> Unit,
    saveImageForLegacyStorage: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveImageForScopedStorage()
    } else {
        saveImageForLegacyStorage()
    }
}
