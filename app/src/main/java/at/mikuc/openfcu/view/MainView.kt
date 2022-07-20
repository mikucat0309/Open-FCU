package at.mikuc.openfcu.view

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.util.currentRoute
import at.mikuc.openfcu.viewmodel.CourseSearchViewModel
import at.mikuc.openfcu.viewmodel.QrCodeViewModel
import at.mikuc.openfcu.viewmodel.RedirectViewModel
import at.mikuc.openfcu.viewmodel.SettingViewModel
import java.io.File

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
    qvm: QrCodeViewModel = hiltViewModel(),
    csvm: CourseSearchViewModel = hiltViewModel(),
) {
    val ctrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(scope, scaffoldState, ctrl.currentRoute()) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
    ) {
        NavHost(
            navController = ctrl,
            startDestination = Graph.QrCode.route,
        ) {
            settingView(svm)
            redirectView(rvm)
            qrcodeView(qvm)
            courseGraph(csvm)
        }
    }
}

private fun NavGraphBuilder.qrcodeView(qvm: QrCodeViewModel) {
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
        val rvm = RedirectViewModel(pref)
        val qvm = QrCodeViewModel(pref)
        MainView(svm, rvm, qvm)
    }
}
