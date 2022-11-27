package at.mikuc.openfcu.redirect

import android.net.Uri
import at.mikuc.openfcu.setting.Credential
import at.mikuc.openfcu.util.UriAsStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSORequest(
    @SerialName("Account") val account: String,
    @SerialName("Password") val password: String,
    @SerialName("RedirectService") val service: String,
) {
    constructor(cred: Credential, service: String) : this(cred.id, cred.password, service)
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
