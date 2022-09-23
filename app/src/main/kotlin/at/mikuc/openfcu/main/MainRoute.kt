package at.mikuc.openfcu.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import at.mikuc.openfcu.appCurrentDestinationAsState
import at.mikuc.openfcu.course.search.CourseSearchFAB
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.destinations.CourseSearchViewDestination
import at.mikuc.openfcu.destinations.Destination
import at.mikuc.openfcu.destinations.DirectionDestination
import at.mikuc.openfcu.destinations.QRCodeViewDestination
import at.mikuc.openfcu.destinations.RedirectViewDestination
import at.mikuc.openfcu.destinations.SettingViewDestination
import at.mikuc.openfcu.destinations.TimetableViewDestination
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow

sealed class MainRoute(
    val dest: DirectionDestination,
    val icon: ImageVector,
    val label: String
) {

    @Composable
    open fun topBar() {
        val route = LocalNavHostController.currentOrThrow.mainRoute
        MainTopAppBar(route?.label ?: "Open FCU")
    }

    @Composable
    open fun floatingActionButton() {
    }

    object Redirect : MainRoute(
        RedirectViewDestination,
        Icons.Outlined.OpenInBrowser,
        "快速跳轉",
    )

    object Course : MainRoute(
        CourseSearchViewDestination,
        Icons.Outlined.Search,
        "課程查詢",
    ) {
        @Composable
        override fun floatingActionButton() {
            CourseSearchFAB()
        }
    }

    object CourseResult : MainRoute(
        CourseSearchResultViewDestination,
        Icons.Outlined.Search,
        "查詢結果",
    )

    object QrCode : MainRoute(
        QRCodeViewDestination,
        Icons.Outlined.QrCode,
        "QR Code",
    )

    object Timetable : MainRoute(
        TimetableViewDestination,
        Icons.Outlined.CalendarMonth,
        "課表",
    )

    object Setting : MainRoute(
        SettingViewDestination,
        Icons.Outlined.Settings,
        "設定"
    )
}

val NavHostController.mainRoute: MainRoute?
    @Composable get() = appCurrentDestinationAsState().value?.toMainRoute()

fun Destination.toMainRoute(): MainRoute = when (this) {
    CourseSearchViewDestination -> MainRoute.Course
    CourseSearchResultViewDestination -> MainRoute.CourseResult
    QRCodeViewDestination -> MainRoute.QrCode
    RedirectViewDestination -> MainRoute.Redirect
    SettingViewDestination -> MainRoute.Setting
    TimetableViewDestination -> MainRoute.Timetable
}
