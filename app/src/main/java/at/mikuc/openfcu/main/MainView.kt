package at.mikuc.openfcu.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.course.courseGraph
import at.mikuc.openfcu.pass.passView
import at.mikuc.openfcu.qrcode.QRCodeView
import at.mikuc.openfcu.redirect.RedirectView
import at.mikuc.openfcu.setting.SettingView
import at.mikuc.openfcu.theme.OpenFCUTheme
import at.mikuc.openfcu.timetable.TimetableView

@Composable
fun MainView(startDest: String) {
    val ctrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(ctrl, scope, scaffoldState) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
        floatingActionButton = {
            MyFAB(ctrl)
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
                settingView()
                redirectView()
                qrcodeView()
                courseGraph()
                timetableView()
                passView()
            }
        }
    }
}

private fun NavGraphBuilder.qrcodeView() {
    composable(RootGraph.QrCode.route) {
        QRCodeView()
    }
}

private fun NavGraphBuilder.redirectView() {
    composable(RootGraph.Redirect.route) {
        RedirectView()
    }
}

private fun NavGraphBuilder.settingView() {
    composable(RootGraph.Setting.route) {
        SettingView()
    }
}

private fun NavGraphBuilder.timetableView() {
    composable(RootGraph.Timetable.route) {
        TimetableView()
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenFCUTheme {
        MainView(startDest = RootGraph.Redirect.route)
    }
}
