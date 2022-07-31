package at.mikuc.openfcu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.util.currentRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyDrawer(
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val appGraphs = listOf(
        Graph.Redirect,
        Graph.QrCode,
        Graph.Course,
        Graph.Timetable,
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
            text = "Open FCU",
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
                isSelected = graph.route == ctrl.currentRoute()
            ) {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
                ctrl.navigate(graph.route) {
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
                isSelected = graph.route == ctrl.currentRoute()
            ) {
                ctrl.navigate(graph.route) {
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
fun DrawerButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
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
    OpenFCUTheme {
        Column {
            DrawerButton(text = "Selected", icon = Icons.Outlined.Public, isSelected = true) {}
            DrawerButton(text = "Not selected", icon = Icons.Outlined.Public, isSelected = false) {}
        }
    }
}