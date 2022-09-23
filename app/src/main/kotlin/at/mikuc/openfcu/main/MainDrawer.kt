package at.mikuc.openfcu.main

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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.appCurrentDestinationAsState
import at.mikuc.openfcu.theme.OpenFCUTheme
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.launch

@Composable
fun MainDrawer(
    appRoute: Array<MainRoute>,
    systemRoute: Array<MainRoute>,
    scaffoldState: ScaffoldState = LocalScaffoldState.currentOrThrow,
) {
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
        DrawerButtonList(appRoute, scaffoldState)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        DrawerButtonList(systemRoute, scaffoldState)
    }
}

@Composable
private fun DrawerButtonList(
    items: Array<MainRoute>,
    scaffoldState: ScaffoldState = LocalScaffoldState.currentOrThrow
) {
    val scope = rememberCoroutineScope()
    val nav = LocalNavHostController.currentOrThrow
    items.forEach { route ->
        DrawerButton(
            text = route.label,
            icon = route.icon,
            isSelected = route.dest == nav.appCurrentDestinationAsState().value
        ) {
            scope.launch {
                scaffoldState.drawerState.close()
            }
            nav.navigate(route.dest)
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
            DrawerButton(
                text = "Selected",
                icon = Icons.Outlined.Search,
                isSelected = true
            ) {}
            DrawerButton(
                text = "Not selected",
                icon = Icons.Outlined.Search,
                isSelected = false
            ) {}
        }
    }
}
