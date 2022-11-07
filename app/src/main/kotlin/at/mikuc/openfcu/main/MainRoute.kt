package at.mikuc.openfcu.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import at.mikuc.openfcu.appCurrentDestinationAsState
import at.mikuc.openfcu.course.detail.CourseDetailTAB
import at.mikuc.openfcu.destinations.CourseAssessmentViewDestination
import at.mikuc.openfcu.destinations.CourseInfoViewDestination
import at.mikuc.openfcu.destinations.CoursePreCourseViewDestination
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.destinations.CourseSearchViewDestination
import at.mikuc.openfcu.destinations.DailyTimetableViewDestination
import at.mikuc.openfcu.destinations.Destination
import at.mikuc.openfcu.destinations.HomeViewDestination
import at.mikuc.openfcu.destinations.QRCodeViewDestination
import at.mikuc.openfcu.destinations.RedirectViewDestination
import at.mikuc.openfcu.destinations.SettingViewDestination

sealed class MainRoute(val destination: Destination) {

    @Composable
    open fun TAB() {
    }

    @Composable
    open fun FAB() {
    }

    object Home : MainRoute(HomeViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("Open FCU")
        }
    }

    object Redirect : MainRoute(RedirectViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("快速跳轉")
        }
    }

    object CourseSearch : MainRoute(CourseSearchViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("課程查詢")
        }
    }

    object CourseSearchResult : MainRoute(CourseSearchResultViewDestination) {
        @Composable
        override fun TAB() {
            BackTopAppBar("查詢結果")
        }
    }

    object CourseInfo : MainRoute(CourseInfoViewDestination) {
        @Composable
        override fun TAB() {
            CourseDetailTAB("課程資訊")
        }
    }

    object CoursePreCourse : MainRoute(CoursePreCourseViewDestination) {
        @Composable
        override fun TAB() {
            CourseDetailTAB("前置課程")
        }
    }

    object CourseAssessment : MainRoute(CourseAssessmentViewDestination) {
        @Composable
        override fun TAB() {
            CourseDetailTAB("評量方式")
        }
    }

    object QrCode : MainRoute(QRCodeViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("QR Code")
        }
    }

    object Timetable : MainRoute(DailyTimetableViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("課表")
        }
    }

    object Setting : MainRoute(SettingViewDestination) {
        @Composable
        override fun TAB() {
            MainTopAppBar("設定")
        }
    }
}

val NavHostController.mainRoute: MainRoute?
    @Composable get() = appCurrentDestinationAsState().value?.toMainRoute()

fun Destination.toMainRoute(): MainRoute = when (this) {
    HomeViewDestination -> MainRoute.Home
    CourseSearchViewDestination -> MainRoute.CourseSearch
    CourseSearchResultViewDestination -> MainRoute.CourseSearchResult
    QRCodeViewDestination -> MainRoute.QrCode
    RedirectViewDestination -> MainRoute.Redirect
    SettingViewDestination -> MainRoute.Setting
    DailyTimetableViewDestination -> MainRoute.Timetable
    CourseInfoViewDestination -> MainRoute.CourseInfo
    CoursePreCourseViewDestination -> MainRoute.CoursePreCourse
    CourseAssessmentViewDestination -> MainRoute.CourseAssessment
}
