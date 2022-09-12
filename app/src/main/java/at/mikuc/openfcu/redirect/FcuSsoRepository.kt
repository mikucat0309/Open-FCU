package at.mikuc.openfcu.redirect

import at.mikuc.openfcu.util.catchNetworkException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Inject
import kotlinx.serialization.json.Json

const val SSO_URL = "https://service206-sds.fcu.edu.tw/mobileservice/RedirectService.svc/Redirect"

class FcuSsoRepository @Inject constructor() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
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
}
