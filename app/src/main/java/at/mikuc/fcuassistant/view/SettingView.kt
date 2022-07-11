package at.mikuc.fcuassistant.view

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import at.mikuc.fcuassistant.SettingViewModel
import at.mikuc.fcuassistant.UserPreferencesRepository
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import java.io.File

@Composable
fun SettingView(viewModel: SettingViewModel = hiltViewModel(), navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        var passwordVisible by remember { mutableStateOf(false) }
        TextField(
            value = viewModel.id,
            onValueChange = viewModel.onIDChange,
            placeholder = { Text("NID") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            )
        )
        TextField(
            value = viewModel.password,
            onValueChange = viewModel.onPasswordChange,
            placeholder = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
            navController.navigate(Graph.Redirect.route)
        }) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    FCUAssistantTheme {
        SettingView(
            SettingViewModel(
                UserPreferencesRepository(
                    PreferenceDataStoreFactory.create() {
                        return@create File("")
                    }
                )
            ),
            NavController(Activity())
        )
    }
}