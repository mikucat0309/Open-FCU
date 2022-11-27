package at.mikuc.openfcu.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object IntRangeSerializer : KSerializer<IntRange> {
    override val descriptor = PrimitiveSerialDescriptor("IntRange", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IntRange) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): IntRange {
        val string = decoder.decodeString()
        val (a, b) = string.split("..", limit = 2)
        return IntRange(a.toInt(), b.toInt())
    }
}
