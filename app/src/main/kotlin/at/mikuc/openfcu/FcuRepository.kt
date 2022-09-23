package at.mikuc.openfcu

import android.util.Log
import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.search.SearchFilter
import at.mikuc.openfcu.redirect.SSORequest
import at.mikuc.openfcu.redirect.SSOResponse
import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.timetable.Section
import at.mikuc.openfcu.timetable.TimetableResponseDTO
import at.mikuc.openfcu.util.catchNetworkException
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

private const val COURSE_SEARCH_URL = "https://coursesearch04.fcu.edu.tw/Service/Search.asmx/GetType2Result"
private const val QRCODE_LOGIN_URL = "https://service202-sds.fcu.edu.tw/FcucardQrcode/Login.aspx"
private const val QRCODE_DATA_URL = "https://service202-sds.fcu.edu.tw/FcucardQrcode/FcuCard.aspx/GetEncryptData"
private const val SSO_URL = "https://service206-sds.fcu.edu.tw/mobileservice/RedirectService.svc/Redirect"
private const val TIMETABLE_DATA_URL = "https://service206-sds.fcu.edu.tw/mobileservice/CourseService.svc/Timetable2"

class FcuRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(HttpCookies)
    }

    suspend fun search(filter: SearchFilter): List<Course>? {
        return catchNetworkException {
            Course.fromDTO(client.post(COURSE_SEARCH_URL) {
                contentType(ContentType.Application.Json)
                setBody(filter.toDTO())
            }.body())
        }
    }

    suspend fun fetchQrcode(credential: Credential): String? {
        return if (loginQrcode(credential)) fetchQrcodeData() else null
    }

    private suspend fun loginQrcode(cred: Credential): Boolean {
        val resp = catchNetworkException {
            client.post(QRCODE_LOGIN_URL) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody("username=${cred.id}&password=${cred.password}&appversion=2")
            }
        } ?: return false
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

    suspend fun singleSignOn(request: SSORequest): SSOResponse? {
        return catchNetworkException {
            client.post(SSO_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    suspend fun fetchTimetable(credential: Credential): List<Section>? {
        return catchNetworkException {
            val resp = client.post(TIMETABLE_DATA_URL) {
                contentType(ContentType.Application.Json)
                setBody(credential)
            }.body<TimetableResponseDTO>()
            resp.timeTableTw?.map { it.toSection() }?.toList()
        }
    }
}
