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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.course.addCourseGraph
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.pass.addPassView
import at.mikuc.openfcu.qrcode.addQrcodeView
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.redirect.addRedirectView
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.setting.addSettingView
import at.mikuc.openfcu.theme.OpenFCUTheme
import at.mikuc.openfcu.timetable.addTimetableView

@Composable
fun MainView(startDest: String) {
    val ctrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val svm = hiltViewModel<SettingViewModel>()
    val rvm = hiltViewModel<RedirectViewModel>()
    val csvm = hiltViewModel<CourseSearchViewModel>()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(ctrl, scope, scaffoldState) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
        floatingActionButton = { MyFAB(ctrl) }
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
                addSettingView(svm)
                addRedirectView(rvm)
                addQrcodeView()
                addCourseGraph(csvm)
                addTimetableView()
                addPassView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    OpenFCUTheme {
        MainView(startDest = RootGraph.Redirect.route)
    }
}
