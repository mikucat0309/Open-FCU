package at.mikuc.openfcu.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import kotlinx.coroutines.launch

@Composable
fun MainTopAppBar(
    title: String,
    scaffoldState: ScaffoldState = LocalScaffoldState.currentOrThrow
) {
    TopAppBar(
        title = {
            Row(Modifier.padding(start = 16.dp)) {
                Text(title)
            }
        },
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


@Composable
fun BackTopAppBar(title: String) {
    val controller = LocalNavHostController.currentOrThrow
    TopAppBar(
        title = {
            Row(Modifier.padding(start = 16.dp)) {
                Text(title)
            }
        },
        navigationIcon = {
            val scope = rememberCoroutineScope()
            IconButton(onClick = { scope.launch { controller.popBackStack() } }) {
                Icon(Icons.Outlined.ArrowBack, "back")
            }
        }
    )
}
