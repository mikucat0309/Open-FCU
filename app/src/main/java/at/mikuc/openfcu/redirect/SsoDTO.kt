package at.mikuc.openfcu.redirect

import android.net.Uri
import at.mikuc.openfcu.util.UriAsStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSORequest(
    @SerialName("Account") val account: String,
    @SerialName("Password") val password: String,
    @SerialName("RedirectService") val redirectSsoService: SsoService,
)

@Serializable
data class SSOResponse(
    @SerialName("Message") val message: String = "",
    @SerialName("RedirectService") val redirectSsoService: SsoService,
    @SerialName("RedirectUrl")
    @Serializable(with = UriAsStringSerializer::class)
    val redirectUri: Uri,
    @SerialName("Success") val success: Boolean,
)

@Serializable
enum class SsoService {
    @SerialName("MyFCU Information System")
    MYFCU,

    @SerialName("iLearn 2.0")
    ILEARN2,
    ;

    companion object {
        fun valueOf2(value: String?): SsoService? {
            if (value == null) return null
            return try {
                valueOf(value)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}
