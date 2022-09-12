package at.mikuc.openfcu

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val name: String,
    val icon: ImageVector,
    val graph: Graph,
)
