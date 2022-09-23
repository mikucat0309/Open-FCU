package at.mikuc.openfcu.redirect

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.theme.OpenFCUTheme
import at.mikuc.openfcu.util.getActivity
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun RedirectView(
    viewModel: RedirectViewModel = getActivityViewModel(),
    settingViewModel: SettingViewModel = getActivityViewModel()
) {
    val items = viewModel.state.redirectItems
    val onClick: (RedirectItem) -> Unit = { viewModel.fetchRedirectToken(it.service) }
    RedirectView2(items, onClick)
    val activity = LocalContext.current.getActivity() ?: return
    val service = activity.intent.getStringExtra("redirect_service")
    if (service != null && settingViewModel.state.isBlank()) {
        viewModel.fetchRedirectToken(service)
    }
    callback(activity, viewModel)
}

@Composable
private fun callback(
    activity: ComponentActivity,
    viewModel: RedirectViewModel
) {
    activity.lifecycleScope.launch {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect {
                if (it.uri != null) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = it.uri
                    }
                    activity.startActivity(intent)
                }
                if (it.message != null) {
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
                viewModel.eventDone()
            }
        }
    }
}

@Composable
private fun RedirectView2(
    items: List<RedirectItem>,
    onClick: (RedirectItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxSize()
    ) {
        items.forEach {
            RedirectRow(title = it.title, icon = it.icon) {
                onClick(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedirectPreview() {
    OpenFCUTheme {
        RedirectView2(
            items = listOf(
                RedirectItem("快速跳轉", ""),
                RedirectItem("快速跳轉", ""),
                RedirectItem("快速跳轉", ""),
            )
        ) {}
    }
}

@Composable
fun RedirectRow(
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
fun RedirectRowPreview() {
    OpenFCUTheme {
        Column {
            RedirectRow("iLearn 2.0", icon = Icons.Outlined.Public) {}
            RedirectRow("MyFCU", icon = Icons.Outlined.Public) {}
            RedirectRow("Add custom target", icon = Icons.Outlined.Add) {}
        }
    }
}
