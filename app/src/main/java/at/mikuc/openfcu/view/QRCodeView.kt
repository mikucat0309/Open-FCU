package at.mikuc.openfcu.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.viewmodel.QrCodeViewModel
import java.io.File

@Composable
fun QRCodeView(viewModel: QrCodeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            bitmap = (viewModel.state.bitmap).asImageBitmap(),
            contentDescription = "QRCode",
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = { viewModel.fetchQrCodeData() }) {
            Text("Refresh")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodePreview() {
    OpenFCUTheme {
        QRCodeView(
            QrCodeViewModel(
                UserPreferencesRepository(
                    PreferenceDataStoreFactory.create {
                        return@create File("")
                    }
                )
            )
        )
    }
}