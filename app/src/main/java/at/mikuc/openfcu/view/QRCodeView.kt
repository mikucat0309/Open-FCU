package at.mikuc.openfcu.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.mikuc.openfcu.util.currentRoute
import at.mikuc.openfcu.viewmodel.QrCodeViewModel

@Composable
fun QRCodeView(ctrl: NavHostController, viewModel: QrCodeViewModel) {
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
}