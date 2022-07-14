package at.mikuc.openfcu

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.view.MainView
import at.mikuc.openfcu.viewmodel.RedirectViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

const val TAG = "OpenFCU"

@HiltAndroidApp
class MainApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rvm: RedirectViewModel by viewModels()
        rvm.state.observe(this) {
            if (it.intent != null) {
                startActivity(it.intent)
                rvm.clearRedirectIntent()
            }
        }
        rvm.state.observe(this) {
            if (it.toastMessage != null) {
                Toast.makeText(this, it.toastMessage, Toast.LENGTH_LONG).show()
                rvm.clearToast()
            }
        }
        setContent {
            OpenFCUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView(rvm = rvm)
                }
            }
        }
    }
}
