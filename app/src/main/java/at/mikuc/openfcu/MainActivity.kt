package at.mikuc.openfcu

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import at.mikuc.openfcu.main.RootGraph
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.redirect.SsoService
import at.mikuc.openfcu.setting.SettingViewModel
import at.mikuc.openfcu.theme.OpenFCUTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

const val TAG = "OpenFCU"

@HiltAndroidApp
class MainApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val svm: SettingViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                svm.event.collect {
                    if (it.toastMessage != null) {
                        Toast.makeText(
                            this@MainActivity,
                            it.toastMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    svm.eventDone()
                }
            }
        }
        val rvm: RedirectViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                rvm.event.collect {
                    if (it.uri != null) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data = it.uri
                        }
                        startActivity(intent)
                    }
                    if (it.message != null) {
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                    rvm.eventDone()
                }
            }
        }

        val hasStoredCredential = svm.state.id.isNotBlank() && svm.state.password.isNotBlank()
        val startDest = if (hasStoredCredential) RootGraph.Redirect.route
        else RootGraph.Setting.route
        val service = SsoService.optionalValueOf(intent.getStringExtra("redirect_service"))
        if (service != null && hasStoredCredential) {
            val path = intent.getStringExtra("myfcu_path")
            rvm.fetchRedirectToken(service, path)
        }
        setContent {
            OpenFCUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView(startDest = startDest)
                }
            }
        }
    }
}
