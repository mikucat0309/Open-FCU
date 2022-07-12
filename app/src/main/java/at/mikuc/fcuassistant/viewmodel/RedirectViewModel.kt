package at.mikuc.fcuassistant.viewmodel

import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Publish
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class RedirectData(
    val title: String,
    val subtitle: String = "",
    val service: SSOService,
    val path: String? = null,
    val icon: ImageVector = Icons.Outlined.Publish
)

@HiltViewModel
class RedirectViewModel @Inject constructor(
    private val pref: UserPreferencesRepository
) : ViewModel() {

    private val _redirectItems = MutableLiveData(
        listOf(
            RedirectData(
                title = "iLearn 2.0",
                "教學管理系統",
                service = SSOService.ILEARN2,
            ),
            RedirectData(
                title = "MyFCU",
                "校務系統",
                service = SSOService.MYFCU,
            ),
            RedirectData(
                title = "自主健康管理",
                service = SSOService.MYFCU,
                path = "S4301/S430101_temperature_record.aspx",
            ),
            RedirectData(
                title = "空間借用",
                service = SSOService.MYFCU,
                path = "webClientMyFcuMain.aspx#/prog/SP9300003",
            ),
            RedirectData(
                title = "學生請假",
                service = SSOService.MYFCU,
                path = "S3401/s3401_leave.aspx",
            ),
            RedirectData(
                title = "課程檢索",
                service = SSOService.MYFCU,
                path = "coursesearch.aspx?sso",
            ),
        )
    )
    val redirectItems: LiveData<List<RedirectData>> = _redirectItems
    private val _redirectIntent = MutableLiveData<Intent>()
    val redirectIntent: LiveData<Intent> = _redirectIntent
    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    fun redirect(service: SSOService, path: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = pref.get(KEY_ID, "")
            val password = pref.get(KEY_PASSWORD, "")
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
                _redirectIntent.postValue(intent)
            } else {
                Log.w(TAG, response.message)
                _toast.postValue(response.message)
            }
        }
    }

    fun clearRedirectIntent() {
        _redirectIntent.value = null
    }

    fun clearToast() {
        _toast.value = null
    }
}