package at.mikuc.openfcu.qrcode

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination
import io.github.g0dkar.qrcode.QRCode
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun QRCodeView(viewModel: QrcodeViewModel = getActivityViewModel()) {
    val onRefresh = { viewModel.fetchQrcode() }
    val onCopy = {}
    PureQRCodeView(viewModel.hexStr, onRefresh, onCopy)
}

@Composable
private fun PureQRCodeView(
    hexStr: String?,
    onRefresh: () -> Unit,
    onCopy: () -> Unit
) {
    val SIZE = 200
    val defaultImage = remember {
        Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
            .apply { eraseColor(Color.Gray.value.toLong()) }
    }
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) { onRefresh() }
        val bitmap = if (hexStr != null)
            QRCode(hexStr).render(margin = 16).nativeImage() as Bitmap
        else
            defaultImage
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "QR Code",
            modifier = Modifier.size(SIZE.dp)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MyTextButton(Icons.Filled.Refresh,"Refresh", onRefresh)
            MyTextButton(Icons.Filled.ContentCopy,"Copy", onCopy)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
private fun QRCodePreview() {
    MixMaterialTheme {
        PureQRCodeView(hexStr = null, onRefresh = {}, onCopy = {})
    }
}

@Composable
private fun MyTextButton(icon: ImageVector, text: String, onRefresh: () -> Unit) {
    OutlinedButton(
        onClick = onRefresh,
    ) {
        Icon(icon, text)
        Box(Modifier.padding(6.dp, 4.dp)) {
            Text(text.uppercase())
        }
    }
}
