package at.mikuc.openfcu.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.repository.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.openfcu.repository.UserPreferencesRepository.Companion.KEY_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.QRCode.Companion.DEFAULT_CELL_SIZE
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

data class QrcodeUiState(
    val bitmap: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.GRAY)
    },
)

const val QRCODE_LOGIN_URL = "https://service202-sds.fcu.edu.tw/FcucardQrcode/Login.aspx"
const val QRCODE_DATA_URL =
    "https://service202-sds.fcu.edu.tw/FcucardQrcode/FcuCard.aspx/GetEncryptData"

@HiltViewModel
class QrcodeViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
) : ViewModel() {

    var state by mutableStateOf(QrcodeUiState())

    private val client = HttpClient(CIO) {
        followRedirects = false
        install(ContentNegotiation) { json() }
        install(HttpCookies)
    }

    init {
        fetchQrcode()
    }

    fun fetchQrcode() {
        viewModelScope.launch {
            val id = pref.get(KEY_ID) ?: return@launch
            val password = pref.get(KEY_PASSWORD) ?: return@launch
            Log.d(TAG, id)
            if (!loginQrcode(id, password)) return@launch
            val hexStr = fetchQrcodeData() ?: return@launch
            val bitmap = QRCode(hexStr).render(margin = DEFAULT_CELL_SIZE).nativeImage() as Bitmap
            state = state.copy(bitmap = bitmap)
        }
    }

    private suspend fun loginQrcode(id: String, password: String): Boolean {
        val resp = client.post(QRCODE_LOGIN_URL) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("username=$id&password=$password&appversion=2")
        }
        Log.d(TAG, resp.body())
        return resp.status == HttpStatusCode.Found
    }

    private suspend fun fetchQrcodeData(): String? {
        val resp = client.post(QRCODE_DATA_URL) {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {})
        }.body<JsonElement>()
        Log.d(TAG, resp.jsonObject.toString())
        return resp
            .jsonObject["d"]
            ?.jsonObject?.get("hexString")
            ?.jsonPrimitive?.content
    }
}
