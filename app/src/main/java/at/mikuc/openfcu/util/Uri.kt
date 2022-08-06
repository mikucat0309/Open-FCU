package at.mikuc.openfcu.util

import android.net.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

fun Uri.replaceUriParameter(key: String, newValue: String): Uri {
    val params = queryParameterNames
    if (!params.contains(key)) return this
    return with(buildUpon()) {
        clearQuery()
        params.filterNot { it == key }
            .forEach { appendQueryParameter(it, getQueryParameter(it)) }
        appendQueryParameter(key, newValue)
        build()
    }
}

object UriAsStringSerializer : KSerializer<Uri> {
    override val descriptor = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uri {
        val string = decoder.decodeString()
        return Uri.parse(string)
    }
}
