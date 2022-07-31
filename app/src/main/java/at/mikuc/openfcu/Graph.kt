package at.mikuc.openfcu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Graph(val route: String, val displayName: String, val icon: ImageVector) {
    object Redirect : Graph("redirect", "快速跳轉", Icons.Outlined.Apps)
    object Setting : Graph("setting", "設定", Icons.Outlined.Settings)
    object QrCode : Graph("qrcode", "數位 IC 卡 (QRCode)", Icons.Outlined.QrCode)
    object Course : Graph("course_search", "課程檢索", Icons.Outlined.Search)
    object Timetable : Graph("timetable", "課表", Icons.Outlined.CalendarMonth)
    companion object {
        fun getGraph(route: String?): Graph? = when (route) {
            Redirect.route -> Redirect
            Setting.route -> Setting
            QrCode.route -> QrCode
            Course.route -> Course
            Timetable.route -> Timetable
            else -> null
        }
    }
}
