package at.mikuc.fcuassistant.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Public
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.mikuc.fcuassistant.repository.UserPreferencesRepository
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import at.mikuc.fcuassistant.viewmodel.QrCodeViewModel
import at.mikuc.fcuassistant.viewmodel.RedirectViewModel
import at.mikuc.fcuassistant.viewmodel.SettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MainView(
    svm: SettingViewModel = hiltViewModel(),
    rvm: RedirectViewModel = hiltViewModel(),
    qvm: QrCodeViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        topBar = { MyTopBar(currentRoute, scope, scaffoldState) },
        drawerContent = { MyDrawer(currentRoute, scope, scaffoldState, navController) },
        scaffoldState = scaffoldState,
    ) { padding ->
        Box(
            Modifier.padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Graph.QrCode.route,
            ) {
                composable(Graph.Redirect.route) {
                    RedirectView(rvm)
                }
                composable(Graph.Setting.route) {
                    SettingView(svm, navController)
                }
                composable(Graph.QrCode.route) {
                    QRCodeView(qvm)
                }
            }
        }
    }
}

@Composable
private fun MyDrawer(
    currentRoute: String?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    val appGraphs = listOf(
        Graph.Redirect,
        Graph.QrCode,
    )
    val systemGraphs = listOf(
        Graph.Setting,
    )
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
                scope.launch {
                    scaffoldState.drawerState.close()
                }
                navController.navigate(graph.route) {
                    popUpTo(Graph.Redirect.route)
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
}

@Composable
private fun MyTopBar(
    currentRoute: String?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TopAppBar(
        title = {
            Text(text = Graph.getGraph(currentRoute)?.displayName ?: "FCU Assistant")
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
        val qvm = QrCodeViewModel(pref)
        MainView(svm, rvm, qvm)
    }
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
        Column {
            DrawerButton(text = "Selected", icon = Icons.Outlined.Public, isSelected = true) {}
            DrawerButton(text = "Not selected", icon = Icons.Outlined.Public, isSelected = false) {}
        }
    }
}
