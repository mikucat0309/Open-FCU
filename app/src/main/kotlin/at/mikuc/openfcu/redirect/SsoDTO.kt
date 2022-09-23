package at.mikuc.openfcu.redirect

import android.net.Uri
import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.util.UriAsStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSORequest(
    val credential: Credential,
    @SerialName("RedirectService") val redirectSsoService: String,
) {
    @SerialName("Account")
    val account: String = credential.id

    @SerialName("Password")
    val password: String = credential.password
}

@Serializable
data class SSOResponse(
    @SerialName("Message") val message: String? = null,
    @SerialName("RedirectService") val redirectSsoService: String,
    @SerialName("RedirectUrl")
    @Serializable(with = UriAsStringSerializer::class)
    val redirectUri: Uri,
    @SerialName("Success") val success: Boolean,
)
