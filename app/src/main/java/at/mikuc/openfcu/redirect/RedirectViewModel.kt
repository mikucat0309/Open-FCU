package at.mikuc.openfcu.redirect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.setting.UserPreferencesRepository
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_PASSWORD
import at.mikuc.openfcu.util.replaceUriParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RedirectViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
    private val repo: FcuSsoRepository,
) : ViewModel() {

    var state by mutableStateOf(
        RedirectUiState(
            listOf(
                RedirectItem(
                    title = "iLearn 2.0",
                    service = SsoService.ILEARN2,
                ),
                RedirectItem(
                    title = "MyFCU",
                    service = SsoService.MYFCU,
                ),
                RedirectItem(
                    title = "自主健康管理",
                    service = SsoService.MYFCU,
                    path = "S4301/S430101_temperature_record.aspx",
                ),
                RedirectItem(
                    title = "空間借用",
                    service = SsoService.MYFCU,
                    path = "webClientMyFcuMain.aspx#/prog/SP9300003",
                ),
                RedirectItem(
                    title = "學生請假",
                    service = SsoService.MYFCU,
                    path = "S3401/s3401_leave.aspx",
                ),
                RedirectItem(
                    title = "課程檢索",
                    service = SsoService.MYFCU,
                    path = "coursesearch.aspx?sso",
                ),
            )
        )
    )
        private set

    private val _event = MutableStateFlow(RedirectEvent())
    val event: StateFlow<RedirectEvent> = _event

    fun fetchRedirectToken(service: SsoService, path: String? = null) {
        viewModelScope.launch {
            val id = pref.get(KEY_ID) ?: return@launch
            val password = pref.get(KEY_PASSWORD) ?: return@launch
            val response = repo.singleSignOn(SSORequest(id, password, service)) ?: return@launch
            if (response.success) {
                val uri = if (service == SsoService.MYFCU) {
                    response.redirectUri.replaceUriParameter(
                        "url",
                        path ?: "webClientMyFcuMain.aspx#/prog/home"
                    )
                } else response.redirectUri
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
