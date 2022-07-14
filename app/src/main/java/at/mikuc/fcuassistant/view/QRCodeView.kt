package at.mikuc.fcuassistant.view

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import at.mikuc.fcuassistant.viewmodel.QrCodeViewModel

@Composable
fun QRCodeView(viewModel: QrCodeViewModel) {
    val tmpBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
    tmpBitmap.eraseColor(Color.GRAY)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Image(
            bitmap = (viewModel.state.observeAsState().value!!.bitmap ?: tmpBitmap).asImageBitmap(),
            contentDescription = "QRCode",
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = { viewModel.fetchQrCodeData() }) {
            Text("Refresh")
        }
    }
}