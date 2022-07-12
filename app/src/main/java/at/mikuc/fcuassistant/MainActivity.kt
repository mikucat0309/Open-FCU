package at.mikuc.fcuassistant

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
import at.mikuc.fcuassistant.ui.theme.FCUAssistantTheme
import at.mikuc.fcuassistant.view.MainView
import at.mikuc.fcuassistant.viewmodel.RedirectViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

const val TAG = "FCUAssistant"

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
            FCUAssistantTheme {
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
