package at.mikuc.fcuassistant

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.fcuassistant.model.SSORequest
import at.mikuc.fcuassistant.model.SSOResponse
import at.mikuc.fcuassistant.model.SSOService
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

const val TAG = "FCU_Assistant"
val KEY_ID = stringPreferencesKey("ID")
val KEY_PASSWORD = stringPreferencesKey("Password")

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: UserPreferencesRepository
) : ViewModel() {

    var id by mutableStateOf(TextFieldValue(""))
        private set
    var password by mutableStateOf(TextFieldValue(""))
        private set

    val onIDChange: (TextFieldValue) -> Unit = { id = it }
    val onPasswordChange: (TextFieldValue) -> Unit = { password = it }

    init {
        viewModelScope.launch {
            id = TextFieldValue(pref.get(KEY_ID, ""))
            password = TextFieldValue(pref.get(KEY_PASSWORD, ""))
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveConfig()
    }

    fun saveConfig() {
        // TODO(check ID password)
        viewModelScope.launch {
            pref.set(KEY_ID, id.text)
            pref.set(KEY_PASSWORD, password.text)
        }
    }
}

@HiltViewModel
class RedirectViewModel @Inject constructor(
    private val pref: UserPreferencesRepository
) : ViewModel() {

    val redirectIntent = MutableLiveData<Intent>()
    val toast = MutableLiveData<String>()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    fun redirect(service: SSOService, path: String = "webClientMyFcuMain.aspx#/prog/home") {
        viewModelScope.launch(Dispatchers.IO) {
            val id = pref.get(KEY_ID, "")
            val password = pref.get(KEY_PASSWORD, "")
            val data = SSORequest(id, password, service)
            val response: SSOResponse =
                client.post("https://service206-sds.fcu.edu.tw/mobileservice/RedirectService.svc/Redirect") {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }.body()
            Log.i(TAG, Json.encodeToString(response))
            if (response.success) {
                val uri = response.redirectUri.replaceUriParameter("url", path)
                Log.d(TAG, uri.toString())
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    setData(uri)
                }
                redirectIntent.postValue(intent)
            } else {
                Log.w(TAG, response.message)
                toast.postValue(response.message)
            }
        }
    }
}
