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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.course.courseGraph
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.main.MyDrawer
import at.mikuc.openfcu.main.MyFAB
import at.mikuc.openfcu.main.MyTopBar
import at.mikuc.openfcu.main.RootGraph
import at.mikuc.openfcu.pass.passView
import at.mikuc.openfcu.qrcode.QRCodeView
import at.mikuc.openfcu.qrcode.QrcodeViewModel
import at.mikuc.openfcu.redirect.RedirectView
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.setting.SettingView
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.timetable.TimetableView
import at.mikuc.openfcu.timetable.TimetableViewModel
import at.mikuc.openfcu.theme.OpenFCUTheme

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
    qvm: QrcodeViewModel = hiltViewModel(),
    csvm: CourseSearchViewModel = hiltViewModel(),
    ttvm: TimetableViewModel = hiltViewModel(),
    startDest: String,
) {
    val ctrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(ctrl, scope, scaffoldState) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
        floatingActionButton = {
            MyFAB(ctrl, csvm)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = ctrl,
                startDestination = startDest,
            ) {
                settingView(svm)
                redirectView(rvm)
                qrcodeView(qvm)
                courseGraph(csvm)
                timetableView(ttvm)
                passView()
            }
        }
    }
}

private fun NavGraphBuilder.qrcodeView(qvm: QrcodeViewModel) {
    composable(RootGraph.QrCode.route) {
        QRCodeView(qvm)
    }
}

private fun NavGraphBuilder.redirectView(rvm: RedirectViewModel) {
    composable(RootGraph.Redirect.route) {
        RedirectView(rvm)
    }
}

private fun NavGraphBuilder.settingView(svm: SettingViewModel) {
    composable(RootGraph.Setting.route) {
        SettingView(svm)
    }
}

private fun NavGraphBuilder.timetableView(ttvm: TimetableViewModel) {
    composable(RootGraph.Timetable.route) {
        TimetableView(ttvm)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenFCUTheme {
        MainView(startDest = RootGraph.Redirect.route)
    }
}
