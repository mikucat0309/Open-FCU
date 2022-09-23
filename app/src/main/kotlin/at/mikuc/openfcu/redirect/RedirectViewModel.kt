package at.mikuc.openfcu.redirect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.FcuRepository
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.setting.UserPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class RedirectViewModel(
    private val pref: UserPreferenceRepository,
    private val repo: FcuRepository,
) : ViewModel(), KoinComponent {

    var state by mutableStateOf(RedirectUiState())
        private set
    private val _event = MutableStateFlow(RedirectEvent())
    val event: StateFlow<RedirectEvent> = _event

    fun fetchRedirectToken(service: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val credential = pref.getCredential().nonBlanked() ?: return@launch
            val response = repo.singleSignOn(SSORequest(credential, service)) ?: return@launch
            if (response.success) {
                val uri = response.redirectUri
                Log.i(TAG, uri.toString())
                _event.value = event.value.copy(uri = uri)
            } else {
                Log.w(TAG, response.message ?: "null")
                _event.value = event.value.copy(message = response.message)
            }
        }
    }

    fun eventDone() {
        _event.value = RedirectEvent(null, null)
    }
}
