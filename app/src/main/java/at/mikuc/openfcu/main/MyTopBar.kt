package at.mikuc.openfcu.main

import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import at.mikuc.openfcu.util.currentGraph
import kotlinx.coroutines.CoroutineScope

@Composable
fun MyTopBar(
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val graph = ctrl.currentGraph()
    TopAppBar(
        title = { MyTitle(graph) },
        navigationIcon = { MyNavigationIcon(ctrl, scope, scaffoldState) },
    )
}
