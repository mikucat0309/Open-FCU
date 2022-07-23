package at.mikuc.openfcu.repository

import android.util.Log
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.data.QRCODE_DATA_URL
import at.mikuc.openfcu.data.QRCODE_LOGIN_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

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
        val resp = client.post(QRCODE_LOGIN_URL) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody("username=$id&password=$password&appversion=2")
        }
        Log.d(TAG, resp.status.value.toString())
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