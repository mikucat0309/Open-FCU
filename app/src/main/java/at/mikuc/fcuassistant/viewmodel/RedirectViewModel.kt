package at.mikuc.fcuassistant.viewmodel

import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.fcuassistant.TAG
import at.mikuc.fcuassistant.data.SSORequest
import at.mikuc.fcuassistant.data.SSOResponse
import at.mikuc.fcuassistant.data.SSOService
import at.mikuc.fcuassistant.repository.UserPreferencesRepository
import at.mikuc.fcuassistant.repository.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.fcuassistant.repository.UserPreferencesRepository.Companion.KEY_PASSWORD
import at.mikuc.fcuassistant.util.replaceUriParameter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class RedirectUiState(
    val redirectItems: List<RedirectItem>,
    val intent: Intent? = null,
    val toastMessage: String? = null,
)

data class RedirectItem(
    val title: String,
    val service: SSOService,
    val path: String? = null,
    val icon: ImageVector = Icons.Outlined.Public
)

@HiltViewModel
class RedirectViewModel @Inject constructor(
    private val pref: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableLiveData(
        RedirectUiState(
            listOf(
                RedirectItem(
                    title = "iLearn 2.0",
                    service = SSOService.ILEARN2,
                ),
                RedirectItem(
                    title = "MyFCU",
                    service = SSOService.MYFCU,
                ),
                RedirectItem(
                    title = "自主健康管理",
                    service = SSOService.MYFCU,
                    path = "S4301/S430101_temperature_record.aspx",
                ),
                RedirectItem(
                    title = "空間借用",
                    service = SSOService.MYFCU,
                    path = "webClientMyFcuMain.aspx#/prog/SP9300003",
                ),
                RedirectItem(
                    title = "學生請假",
                    service = SSOService.MYFCU,
                    path = "S3401/s3401_leave.aspx",
                ),
                RedirectItem(
                    title = "課程檢索",
                    service = SSOService.MYFCU,
                    path = "coursesearch.aspx?sso",
                ),
            )
        )
    )
    val state: LiveData<RedirectUiState> = _state

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    fun redirect(service: SSOService, path: String? = null) {
        viewModelScope.launch {
            val id = pref.get(KEY_ID) ?: return@launch
            val password = pref.get(KEY_PASSWORD) ?: return@launch
            val data = SSORequest(id, password, service)
            val response: SSOResponse =
                client.post("https://service206-sds.fcu.edu.tw/mobileservice/RedirectService.svc/Redirect") {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }.body()
            Log.d(TAG, Json.encodeToString(response))
            if (response.success) {
                val uri = if (service == SSOService.MYFCU) {
                    response.redirectUri.replaceUriParameter(
                        "url",
                        path ?: "webClientMyFcuMain.aspx#/prog/home"
                    )
                } else response.redirectUri
                Log.d(TAG, uri.toString())
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    setData(uri)
                }
                _state.value = _state.value?.copy(intent = intent)
            } else {
                Log.w(TAG, response.message)
                _state.value = _state.value?.copy(toastMessage = response.message)
            }
        }
    }

    fun clearRedirectIntent() {
        _state.value = _state.value?.copy(intent = null)
    }

    fun clearToast() {
        _state.value = _state.value?.copy(toastMessage = null)
    }
}