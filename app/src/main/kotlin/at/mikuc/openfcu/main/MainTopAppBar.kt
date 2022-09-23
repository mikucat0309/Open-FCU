package at.mikuc.openfcu.main

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import at.mikuc.openfcu.util.currentOrThrow
import kotlinx.coroutines.launch

@Composable
fun MainTopAppBar(
    title: String,
    scaffoldState: ScaffoldState = LocalScaffoldState.currentOrThrow
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            val scope = rememberCoroutineScope()
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.apply { if (isClosed) open() else close() }
                }
            }) {
                Icon(Icons.Outlined.Menu, "menu")
            }
        }
    )
}

