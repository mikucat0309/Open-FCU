package at.mikuc.fcuassistant.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import at.mikuc.fcuassistant.RedirectViewModel
import at.mikuc.fcuassistant.UserPreferencesRepository
import at.mikuc.fcuassistant.model.Service
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.io.File

@Composable
fun RedirectView(viewModel: RedirectViewModel = hiltViewModel()) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        RedirectItem(title = "MyFCU", "校務系統", icon = Icons.Outlined.Public) {
            viewModel.redirect(Service.MYFCU)
        }
        RedirectItem(title = "iLearn 2.0", "教學管理系統", icon = Icons.Outlined.Public) {
            viewModel.redirect(Service.ILEARN2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectPreview() {
    FCUAssistantTheme {
        RedirectView(
            RedirectViewModel(
                UserPreferencesRepository(
                    PreferenceDataStoreFactory.create() {
                        return@create File("")
                    }
                )
            )
        )
    }
}

@Composable
fun RedirectItem(
    title: String,
    subtitle: String = title,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            icon, "",
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(title, style = MaterialTheme.typography.body1)
            Text(subtitle, style = MaterialTheme.typography.body2, modifier = Modifier.alpha(0.6f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectCardPreview() {
    FCUAssistantTheme {
        Column {
            RedirectItem("iLearn 2.0", icon = Icons.Outlined.Public) {}
            RedirectItem("MyFCU", icon = Icons.Outlined.Public) {}
            RedirectItem("Add custom target", icon = Icons.Outlined.Add) {}
        }
    }
}

@Composable
fun VerticalReorderList() {
    val data = remember {
        mutableStateOf(
            listOf(
                Service.MYFCU.name,
                Service.ILEARN2.name,
                "Item 3",
                "Item 4",
            )
        )
    }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(MaterialTheme.colors.surface)
                ) {
                    Text(item)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun DragAndDropListPreview() {
    FCUAssistantTheme {
        VerticalReorderList()
    }
}