package at.mikuc.openfcu.redirect

import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.theme.M3
import at.mikuc.openfcu.theme.MixMaterialTheme
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
    val items = viewModel.links
    val onClick: (RedirectItem) -> Unit = { viewModel.fetchRedirectToken(it.service) }
    PureRedirectView(items, onClick)

    val activity = LocalContext.current.getActivity() ?: return
    val service = activity.intent.getStringExtra("redirect_service")
    if (service != null && settingViewModel.state.isBlank()) {
        viewModel.fetchRedirectToken(service)
    }
    redirectCallback(activity, viewModel)
}

@Composable
fun redirectCallback(
    activity: ComponentActivity,
    viewModel: RedirectViewModel
) {
    activity.lifecycleScope.launch {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uri.collect {
                if (it == null) return@collect
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = it
                }
                activity.startActivity(intent)
                viewModel.receivedUri()
            }
        }
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.msg.collect {
                if (it == null) return@collect
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                viewModel.receivedMsg()
            }
        }
    }
}

@Composable
private fun PureRedirectView(
    items: List<RedirectItem>,
    onClick: (RedirectItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items) { item ->
            LinkButton(label = item.title) {
                onClick(item)
            }
        }
    }
}

@Composable
fun LinkButton(label: String, onClick: () -> Unit = {}) {
    Column(
        Modifier
            .requiredWidth(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(M3.Light.surface3)
        ) {}
        Spacer(Modifier.height(4.dp))
        Box(
            Modifier.height(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                label,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
fun PureRedirectPreview() {
    MixMaterialTheme {
        PureRedirectView(
            items = listOf(
                RedirectItem("iLearn 2.0", ""),
                RedirectItem("MyFCU", ""),
                RedirectItem("空間借用", ""),
                RedirectItem("學生請假", ""),
                RedirectItem("行動刷卡", ""),
            )
        ) {}
    }
}
