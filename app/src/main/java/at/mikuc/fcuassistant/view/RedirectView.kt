package at.mikuc.fcuassistant.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Public
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import at.mikuc.fcuassistant.repository.UserPreferencesRepository
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import at.mikuc.fcuassistant.viewmodel.RedirectViewModel
import java.io.File

@Composable
fun RedirectView(viewModel: RedirectViewModel = hiltViewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        viewModel.state.value?.redirectItems?.forEach {
            RedirectItem(title = it.title, it.subtitle, icon = it.icon) {
                viewModel.redirect(it.service, it.path)
            }
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
                    PreferenceDataStoreFactory.create {
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
