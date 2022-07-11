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
import at.mikuc.fcuassistant.RedirectViewModel
import at.mikuc.fcuassistant.UserPreferencesRepository
import at.mikuc.fcuassistant.model.SSOService
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import java.io.File

@Composable
fun RedirectView(viewModel: RedirectViewModel = hiltViewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        RedirectItem(title = "iLearn 2.0", "教學管理系統", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.ILEARN2)
        }
        RedirectItem(title = "MyFCU", "校務系統", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.MYFCU)
        }
        RedirectItem(title = "自主健康管理", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.MYFCU, "S4301/S430101_temperature_record.aspx")
        }
        RedirectItem(title = "空間借用", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.MYFCU, "webClientMyFcuMain.aspx#/prog/SP9300003")
        }
        RedirectItem(title = "學生請假", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.MYFCU, "S3401/s3401_leave.aspx")
        }
        RedirectItem(title = "課程檢索", icon = Icons.Outlined.Public) {
            viewModel.redirect(SSOService.MYFCU, "coursesearch.aspx?sso")
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
