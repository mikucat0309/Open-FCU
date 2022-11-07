package at.mikuc.openfcu.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.NavGraphs
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun MainView() {
    val appRoute = arrayOf(
        DrawerItem.Home,
        DrawerItem.Redirect,
        DrawerItem.QrCode,
        DrawerItem.CourseSearch,
        DrawerItem.Timetable,
    )
    val systemRoute = arrayOf<DrawerItem>(
        DrawerItem.Setting
    )
    CompositionLocalProvider(
        LocalScaffoldState provides rememberScaffoldState(),
        LocalNavHostController provides rememberNavController()
    ) {
        val route = LocalNavHostController.currentOrThrow.mainRoute
        Scaffold(
            scaffoldState = LocalScaffoldState.currentOrThrow,
            topBar = { route?.TAB() },
            drawerContent = { MainDrawer(appRoute, systemRoute) },
            floatingActionButton = { route?.FAB() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = LocalNavHostController.currentOrThrow,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun MainPreview() {
    MixMaterialTheme {
        MainView()
    }
}
