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
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rvm: RedirectViewModel by viewModels()
        rvm.redirectIntent.observe(this) {
            if (it != null) {
                startActivity(it)
                rvm.redirectIntent.value = null
            }
        }
        rvm.toast.observe(this) {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                rvm.toast.value = null
            }
        }
        setContent {
            FCUAssistantTheme {
                // A surface container using the 'background' color from the theme
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
