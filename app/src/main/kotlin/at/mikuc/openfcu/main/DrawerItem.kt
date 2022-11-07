package at.mikuc.openfcu.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import at.mikuc.openfcu.destinations.CourseSearchViewDestination
import at.mikuc.openfcu.destinations.DailyTimetableViewDestination
import at.mikuc.openfcu.destinations.DirectionDestination
import at.mikuc.openfcu.destinations.HomeViewDestination
import at.mikuc.openfcu.destinations.QRCodeViewDestination
import at.mikuc.openfcu.destinations.RedirectViewDestination
import at.mikuc.openfcu.destinations.SettingViewDestination

sealed class DrawerItem(
    val dest: DirectionDestination,
    val icon: ImageVector,
    val label: String
) {

    object Home : DrawerItem(
        HomeViewDestination,
        Icons.Outlined.Home,
        "Open FCU",
    )

    object Redirect : DrawerItem(
        RedirectViewDestination,
        Icons.Outlined.OpenInBrowser,
        "快速跳轉",
    )

    object CourseSearch : DrawerItem(
        CourseSearchViewDestination,
        Icons.Outlined.Search,
        "課程查詢",
    )

    object QrCode : DrawerItem(
        QRCodeViewDestination,
        Icons.Outlined.QrCode,
        "QR Code",
    )

    object Timetable : DrawerItem(
        DailyTimetableViewDestination,
        Icons.Outlined.CalendarMonth,
        "課表",
    )

    object Setting : DrawerItem(
        SettingViewDestination,
        Icons.Outlined.Settings,
        "設定"
    )
}

