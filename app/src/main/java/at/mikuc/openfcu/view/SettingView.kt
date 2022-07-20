package at.mikuc.openfcu.view

import android.app.Application
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
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.util.currentRoute
import at.mikuc.openfcu.viewmodel.SettingViewModel
import java.io.File

@Composable
fun SettingView(
    ctrl: NavHostController,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar(scope, scaffoldState, ctrl.currentRoute()) },
        drawerContent = { MyDrawer(ctrl, scope, scaffoldState) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxSize()
        ) {
            var passwordVisible by remember { mutableStateOf(false) }
            TextField(
                value = viewModel.state.id,
                onValueChange = { viewModel.update(viewModel.state.copy(id = it)) },
                placeholder = { Text("NID") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                value = viewModel.state.password,
                onValueChange = { viewModel.update(viewModel.state.copy(password = it)) },
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
                ctrl.navigate(Graph.Redirect.route)
            }) {
                Text("儲存")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    OpenFCUTheme {
        SettingView(
            NavHostController(Application()),
            SettingViewModel(
                UserPreferencesRepository(
                    PreferenceDataStoreFactory.create {
                        return@create File("")
                    }
                )
            )
        )
    }
}