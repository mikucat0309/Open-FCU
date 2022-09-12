package at.mikuc.openfcu.qrcode

import android.util.Log
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.util.catchNetworkException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Inject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

const val QRCODE_LOGIN_URL = "https://service202-sds.fcu.edu.tw/FcucardQrcode/Login.aspx"
const val QRCODE_DATA_URL =
    "https://service202-sds.fcu.edu.tw/FcucardQrcode/FcuCard.aspx/GetEncryptData"

class FcuQrcodeRepository @Inject constructor() {
    private val client = HttpClient(CIO) {
        followRedirects = false
        install(ContentNegotiation) { json() }
        install(HttpCookies)
    }

    suspend fun fetchQrcode(id: String, password: String): String? {
        if (!loginQrcode(id, password)) return null
        return fetchQrcodeData()
    }

    private suspend fun loginQrcode(id: String, password: String): Boolean {
        val resp = catchNetworkException {
            client.post(QRCODE_LOGIN_URL) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody("username=$id&password=$password&appversion=2")
            }
        } ?: return false
        Log.d(TAG, resp.status.value.toString())
        return resp.status == HttpStatusCode.Found
    }

    private suspend fun fetchQrcodeData(): String? {
        return catchNetworkException {
            val resp = client.post(QRCODE_DATA_URL) {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {})
            }.body<JsonElement>()
            Log.d(TAG, resp.jsonObject.toString())
            resp.jsonObject["d"]
                ?.jsonObject?.get("hexString")
                ?.jsonPrimitive?.content
        }
    }
}
