package at.mikuc.openfcu.setting

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.getActivity
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun SettingView(viewModel: SettingViewModel = getActivityViewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        var passwordVisible by remember { mutableStateOf(false) }
        TextField(
            value = viewModel.state.id,
            onValueChange = { viewModel.state = viewModel.state.copy(id = it) },
            placeholder = { Text("NID") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            )
        )
        val transformation =
            if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        TextField(
            value = viewModel.state.password,
            onValueChange = { viewModel.state = viewModel.state.copy(password = it) },
            placeholder = { Text("Password") },
            singleLine = true,
            visualTransformation = transformation,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Send
            ),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide" else "Show"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            viewModel.saveConfig()
        }) {
            Text("儲存")
        }
    }
    val activity = LocalContext.current.getActivity() ?: return
    toastCallback(activity, viewModel)
}

@Composable
private fun toastCallback(
    activity: ComponentActivity,
    viewModel: SettingViewModel
) {
    activity.lifecycleScope.launch {
        activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collect {
                if (it.toastMessage != null) {
                    Toast.makeText(activity, it.toastMessage, Toast.LENGTH_SHORT).show()
                }
                viewModel.eventDone()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingPreview() {
    MixMaterialTheme {
        SettingView()
    }
}
