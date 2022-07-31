package at.mikuc.openfcu.redirect

import android.net.Uri

data class RedirectEvent(
    val uri: Uri? = null,
    val message: String? = null,
)