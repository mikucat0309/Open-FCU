package at.mikuc.openfcu.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(val route: String, val displayName: String, val icon: ImageVector) {
    object Redirect : Graph("redirect", "快速跳轉", Icons.Outlined.Apps)
    object Setting : Graph("setting", "設定", Icons.Outlined.Settings)
    object QrCode : Graph("qrcode", "數位 IC 卡 (QRCode)", Icons.Outlined.QrCode)
    companion object {
        fun getGraph(route: String?): Graph? = when (route) {
            Redirect.route -> Redirect
            Setting.route -> Setting
            QrCode.route -> QrCode
            else -> null
        }
    }
}
