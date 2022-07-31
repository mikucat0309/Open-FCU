package at.mikuc.openfcu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.course.CourseGraph
import at.mikuc.openfcu.course.CourseSearchFAB
import at.mikuc.openfcu.course.CourseSearchViewModel
import at.mikuc.openfcu.course.courseGraph
import at.mikuc.openfcu.qrcode.FcuQrcodeRepository
import at.mikuc.openfcu.qrcode.QRCodeView
import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.redirect.FcuSsoRepository
import at.mikuc.openfcu.redirect.RedirectView
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.setting.SettingView
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.setting.UserPreferencesRepository
import at.mikuc.openfcu.timetable.TimetableView
import at.mikuc.openfcu.timetable.TimetableViewModel
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.util.currentRoute
import java.io.File

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
    qvm: QrcodeViewModel = hiltViewModel(),
    csvm: CourseSearchViewModel = hiltViewModel(),
    ttvm: TimetableViewModel = hiltViewModel(),
) {
    val ctrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(scope, scaffoldState, ctrl.currentRoute()) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
        floatingActionButton = {
            when (ctrl.currentRoute()) {
                CourseGraph.Search.route -> CourseSearchFAB(ctrl, csvm)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = ctrl,
                startDestination = Graph.Setting.route,
            ) {
                settingView(svm)
                redirectView(rvm)
                qrcodeView(qvm)
                courseGraph(csvm)
                timetableView(ttvm)
            }
        }
    }
}

private fun NavGraphBuilder.qrcodeView(qvm: QrcodeViewModel) {
    composable(Graph.QrCode.route) {
        QRCodeView(qvm)
    }
}

private fun NavGraphBuilder.redirectView(rvm: RedirectViewModel) {
    composable(Graph.Redirect.route) {
        RedirectView(rvm)
    }
}

private fun NavGraphBuilder.settingView(svm: SettingViewModel) {
    composable(Graph.Setting.route) {
        SettingView(svm)
    }
}

private fun NavGraphBuilder.timetableView(ttvm: TimetableViewModel) {
    composable(Graph.Timetable.route) {
        TimetableView(ttvm)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenFCUTheme {
        val pref = UserPreferencesRepository(
            PreferenceDataStoreFactory.create {
                return@create File("")
            }
        )
        val svm = SettingViewModel(pref)
        val rvm = RedirectViewModel(pref, FcuSsoRepository())
        val qvm = QrcodeViewModel(pref, FcuQrcodeRepository())
        MainView(svm, rvm, qvm)
    }
}
