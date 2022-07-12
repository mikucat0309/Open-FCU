package at.mikuc.fcuassistant.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(val route: String, val displayName: String, val icon: ImageVector) {
    object Redirect : Graph("redirect", "快速跳轉", Icons.Outlined.Apps)
    object Setting : Graph("setting", "設定", Icons.Outlined.Settings)
    companion object {
        fun getGraph(route: String?): Graph? = when (route) {
            Redirect.route -> Redirect
            Setting.route -> Setting
            else -> null
        }
    }
}
