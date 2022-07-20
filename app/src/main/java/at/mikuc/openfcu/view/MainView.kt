package at.mikuc.openfcu.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.viewmodel.QrCodeViewModel
import at.mikuc.openfcu.viewmodel.RedirectViewModel
import at.mikuc.openfcu.viewmodel.SettingViewModel
import java.io.File

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
    qvm: QrCodeViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Graph.QrCode.route,
    ) {
        settingView(navController, svm)
        redirectView(navController, rvm)
        qrcodeView(navController, qvm)
    }
}

private fun NavGraphBuilder.qrcodeView(navController: NavHostController, qvm: QrCodeViewModel) {
    composable(Graph.QrCode.route) {
        QRCodeView(navController, qvm)
    }
}

private fun NavGraphBuilder.redirectView(navController: NavHostController, rvm: RedirectViewModel) {
    composable(Graph.Redirect.route) {
        RedirectView(navController, rvm)
    }
}

private fun NavGraphBuilder.settingView(navController: NavHostController, svm: SettingViewModel) {
    composable(Graph.Setting.route) {
        SettingView(navController, svm)
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
