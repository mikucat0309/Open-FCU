package at.mikuc.fcuassistant.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.mikuc.fcuassistant.RedirectViewModel
import at.mikuc.fcuassistant.SettingViewModel
import at.mikuc.fcuassistant.UserPreferencesRepository
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val appGraphs = listOf(
        Graph.Redirect,
//        Graph.Timetable,
    )
    val systemGraphs = listOf(
        Graph.Setting
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            Graph.Redirect.route -> Graph.Redirect.displayName
                            Graph.Timetable.route -> Graph.Timetable.displayName
                            Graph.Setting.route -> Graph.Setting.displayName
                            else -> "FCU Assistant"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                },
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "FCU Assistant",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                appGraphs.forEach { graph ->
                    DrawerButton(
                        text = graph.displayName,
                        icon = graph.icon,
                        isSelected = graph.route == currentRoute
                    ) {
                        navController.navigate(graph.route) {
                            popUpTo(Graph.Redirect.route)
                        }
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                systemGraphs.forEach { graph ->
                    DrawerButton(
                        text = graph.displayName,
                        icon = graph.icon,
                        isSelected = graph.route == currentRoute
                    ) {
                        navController.navigate(graph.route) {
                            popUpTo(Graph.Redirect.route)
                        }
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
    ) { padding ->
        Box(
            Modifier.padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Graph.Redirect.route,
            ) {
                composable(Graph.Redirect.route) {
                    RedirectView(rvm)
                }
                composable(Graph.Timetable.route) {
                    TimetableView()
                }
                composable(Graph.Setting.route) {
                    SettingView(svm, navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    FCUAssistantTheme {
        val pref = UserPreferencesRepository(
            PreferenceDataStoreFactory.create {
                return@create File("")
            }
        )
        val svm = SettingViewModel(pref)
        val rvm = RedirectViewModel(pref)
        MainView(svm, rvm)
    }
}

@Composable
fun TimetableView() {
    // TODO
}

sealed class Graph(val route: String, val displayName: String, val icon: ImageVector) {
    object Redirect : Graph("Redirect", "跳轉連結", Icons.Outlined.Apps)
    object Timetable : Graph("Timetable", "課程時間表", Icons.Outlined.CalendarToday)
    object Setting : Graph("Setting", "設定", Icons.Outlined.Settings)
}

@Composable
fun DrawerButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors
    val imageAlpha = if (isSelected) 1f else 0.6f
    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.8f)
    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.1f) else colors.surface
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(4.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
    ) {
        TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(icon, text, tint = textIconColor, modifier = Modifier.alpha(imageAlpha))
                Spacer(Modifier.width(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.copy(color = textIconColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerButtonPreview() {
    FCUAssistantTheme {
        DrawerButton(text = "Test", icon = Icons.Outlined.Public, isSelected = true) {

        }
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = UI_MODE_NIGHT_YES,
//)
//@Composable
//fun MainDarkPreview() {
//    FCUAssistantTheme {
//        val pref = UserPreferencesRepository(
//            PreferenceDataStoreFactory.create() {
//                return@create File("")
//            }
//        )
//        val svm = SettingViewModel(pref)
//        val rvm = RedirectViewModel(pref)
//        MainView(svm, rvm)
//    }
//}