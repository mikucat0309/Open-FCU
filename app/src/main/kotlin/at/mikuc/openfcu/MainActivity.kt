package at.mikuc.openfcu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import at.mikuc.openfcu.main.MainView
import at.mikuc.openfcu.theme.MaterialTheme3
import at.mikuc.openfcu.theme.MixMaterialTheme

const val TAG = "OpenFCU"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MixMaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme3.colorScheme.background
                ) {
                    MainView()
                }
            }
        }
    }
}
