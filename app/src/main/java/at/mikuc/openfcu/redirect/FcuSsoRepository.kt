package at.mikuc.openfcu.redirect

import at.mikuc.openfcu.SSO_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FcuSsoRepository @Inject constructor() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    suspend fun singleSignOn(request: SSORequest): SSOResponse {
        return client.post(SSO_URL) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

}