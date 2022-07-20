package at.mikuc.openfcu.view

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyTopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    currentRoute: String?,
) {
    TopAppBar(
        title = {
            Text(text = Graph.getGraph(currentRoute)?.displayName ?: "Open FCU")
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