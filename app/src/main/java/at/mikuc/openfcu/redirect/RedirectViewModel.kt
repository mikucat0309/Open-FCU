package at.mikuc.openfcu.redirect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.setting.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RedirectViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
    private val repo: FcuSsoRepository,
) : ViewModel() {

    var state by mutableStateOf(RedirectUiState())
        private set

    private val _event = MutableStateFlow(RedirectEvent())
    val event: StateFlow<RedirectEvent> = _event

    fun fetchRedirectToken(service: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val credential = pref.getCredential() ?: return@launch
            val request = SSORequest(credential, service)
            val response = repo.singleSignOn(request) ?: return@launch
            if (response.success) {
                val uri = response.redirectUri
                Log.i(TAG, uri.toString())
                _event.value = event.value.copy(uri = uri)
            } else {
                Log.w(TAG, response.message)
                _event.value = event.value.copy(message = response.message)
            }
        }
    }

    fun eventDone() {
        _event.value = RedirectEvent(null, null)
    }
}
