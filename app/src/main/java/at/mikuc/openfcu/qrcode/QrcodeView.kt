package at.mikuc.openfcu.qrcode

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import at.mikuc.openfcu.setting.UserPreferencesRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import io.github.g0dkar.qrcode.QRCode
import java.io.File

@Composable
fun QRCodeView(viewModel: QrcodeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            viewModel.fetchQrcode()
        }
        val hexStr = viewModel.state.hexStr
        val bitmap = if (hexStr != null)
            QRCode(hexStr).render(margin = QRCode.DEFAULT_CELL_SIZE).nativeImage() as Bitmap
        else Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).apply { eraseColor(Color.GRAY) }
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "QRCode",
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = { viewModel.fetchQrcode() }) {
            Text("Refresh")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodePreview() {
    OpenFCUTheme {
        val pref = UserPreferencesRepository(
            PreferenceDataStoreFactory.create {
                return@create File("")
            }
        )
        QRCodeView(
            QrcodeViewModel(pref, FcuQrcodeRepository())
        )
    }
}