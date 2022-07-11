package at.mikuc.fcuassistant.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(val route: String, val displayName: String, val icon: ImageVector) {
    object Redirect : Graph("Redirect", "跳轉連結", Icons.Outlined.Apps)
    object Setting : Graph("Setting", "設定", Icons.Outlined.Settings)
}
