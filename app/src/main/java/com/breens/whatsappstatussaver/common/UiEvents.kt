package com.breens.whatsappstatussaver.common

import android.net.Uri

sealed class UiEvents {
    object NavigateToAskPermissions : UiEvents()

    data class GetWhatsAppStatuses(val uri: Uri) : UiEvents()
}
