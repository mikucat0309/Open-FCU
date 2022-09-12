package at.mikuc.openfcu.redirect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import at.mikuc.openfcu.setting.UserPreferencesRepository
import at.mikuc.openfcu.theme.OpenFCUTheme
import java.io.File

@Composable
fun RedirectView(viewModel: RedirectViewModel = hiltViewModel()) {
    val state = viewModel.state
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        state.redirectItems.forEach {
            RedirectItem(title = it.title, icon = it.icon) {
                viewModel.fetchRedirectToken(it.service, it.path)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectPreview() {
    OpenFCUTheme {
        val pref = UserPreferencesRepository(
            PreferenceDataStoreFactory.create {
                return@create File("")
            }
        )
        RedirectView(
            RedirectViewModel(pref, FcuSsoRepository())
        )
    }
}

@Composable
fun RedirectItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
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
        Text(title, style = MaterialTheme.typography.body1.copy(fontSize = 18.sp))
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectCardPreview() {
    OpenFCUTheme {
        Column {
            RedirectItem("iLearn 2.0", icon = Icons.Outlined.Public) {}
            RedirectItem("MyFCU", icon = Icons.Outlined.Public) {}
            RedirectItem("Add custom target", icon = Icons.Outlined.Add) {}
        }
    }
}
