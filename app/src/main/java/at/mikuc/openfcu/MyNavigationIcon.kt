package at.mikuc.openfcu

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import at.mikuc.openfcu.util.currentGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyNavigationIcon(
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    when (ctrl.currentGraph()) {
        else -> DefaultNavigationIcon(scope, scaffoldState)
    }
}

@Composable
private fun DefaultNavigationIcon(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    IconButton(onClick = {
        scope.launch {
            scaffoldState.drawerState.apply { if (isClosed) open() else close() }
        }
    }) {
        Icon(Icons.Outlined.Menu, "menu")
    }
}
