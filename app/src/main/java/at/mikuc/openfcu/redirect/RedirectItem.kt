package at.mikuc.openfcu.redirect

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.ui.graphics.vector.ImageVector

data class RedirectItem(
    val title: String,
    val service: SsoService,
    val path: String? = null,
    val icon: ImageVector = Icons.Outlined.Public,
)