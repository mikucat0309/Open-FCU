package at.mikuc.openfcu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import at.mikuc.openfcu.util.currentGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyDrawer(
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val appGraphs = listOf(
        DrawerItem("快速跳轉", Icons.Filled.Apps, RootGraph.Redirect),
        DrawerItem("數位 IC 卡 (QRCode)", Icons.Filled.QrCode, RootGraph.QrCode),
        DrawerItem("課程查詢", Icons.Filled.Search, RootGraph.Course),
        DrawerItem("課表", Icons.Filled.CalendarMonth, RootGraph.Timetable),
        DrawerItem("仿造 PASS", Icons.Filled.HealthAndSafety, RootGraph.Pass),
    )
    val systemGraphs = listOf(
        DrawerItem("設定", Icons.Filled.Settings, RootGraph.Setting),
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
        AppDrawerButton(appGraphs, ctrl, scope, scaffoldState)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        SystemDrawerButton(systemGraphs, ctrl, scope, scaffoldState)
    }
}

@Composable
private fun SystemDrawerButton(
    systemGraphs: List<DrawerItem>,
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    systemGraphs.forEach { graph ->
        DrawerButton(
            text = graph.name,
            icon = graph.icon,
            isSelected = ctrl.currentGraph() == graph.graph
        ) {
            ctrl.navigate(graph.graph.route) {
                popUpTo(ctrl.graph.startDestinationRoute!!)
            }
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }
}

@Composable
private fun AppDrawerButton(
    items: List<DrawerItem>,
    ctrl: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    items.forEach { item ->
        DrawerButton(
            text = item.name,
            icon = item.icon,
            isSelected = ctrl.currentGraph() == item.graph
        ) {
            scope.launch {
                scaffoldState.drawerState.close()
            }
            ctrl.navigate(item.graph.route) {
                popUpTo(ctrl.graph.startDestinationRoute!!)
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
