package at.mikuc.fcuassistant.data

import android.net.Uri
import at.mikuc.fcuassistant.util.UriAsStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSORequest(
    @SerialName("Account") val account: String,
    @SerialName("Password") val password: String,
    @SerialName("RedirectService") val redirectSSOService: SSOService,
)

@Serializable
data class SSOResponse(
    @SerialName("Message") val message: String = "",
    @SerialName("RedirectService") val redirectSSOService: SSOService,
    @SerialName("RedirectUrl") @Serializable(with = UriAsStringSerializer::class) val redirectUri: Uri,
    @SerialName("Success") val success: Boolean,
)

@Serializable
enum class SSOService {
    @SerialName("MyFCU Information System")
    MYFCU,

    @SerialName("iLearn 2.0")
    ILEARN2,
    ;
}
