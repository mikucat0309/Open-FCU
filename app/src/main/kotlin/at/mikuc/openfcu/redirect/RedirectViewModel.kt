package at.mikuc.openfcu.redirect

import android.net.Uri
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

    var links by mutableStateOf(
        listOf(
            RedirectItem(
                title = "iLearn 2.0",
                service = "iLearn 2.0",
            ),
            RedirectItem(
                title = "MyFCU",
                service = "https://myfcu.fcu.edu.tw/main/webClientMyFcuMain.aspx",
            ),
            RedirectItem(
                title = "自主健康管理",
                service = "https://myfcu.fcu.edu.tw/main/S4301/S430101_temperature_record.aspx",
            ),
            RedirectItem(
                title = "空間借用",
                service = "https://myfcu.fcu.edu.tw/main/S5672/S5672_mainApply.aspx",
            ),
            RedirectItem(
                title = "學生請假",
                service = "https://myfcu.fcu.edu.tw/main/S3401/s3401_leave.aspx",
            ),
            RedirectItem(
                title = "課程查詢",
                service = "https://myfcu.fcu.edu.tw/main/coursesearch.aspx?sso",
            ),
        )
    )
        private set

    private val _uri = MutableStateFlow<Uri?>(null)
    val uri: StateFlow<Uri?> = _uri

    private val _msg = MutableStateFlow<String?>(null)
    val msg: StateFlow<String?> = _msg

    fun fetchRedirectToken(service: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val credential = pref.getCredential().nonBlanked() ?: return@launch
            val response = repo.singleSignOn(SSORequest(credential, service)) ?: return@launch
            if (response.success) {
                val uri = response.redirectUri
                Log.i(TAG, uri.toString())
                _uri.value = uri
                _msg.value = "Loading ..."
            } else {
                Log.w(TAG, response.message ?: "null")
                _msg.value = response.message
            }
        }
    }

    fun receivedMsg() {
        _msg.value = null
    }

    fun receivedUri() {
        _uri.value = null
    }
}
