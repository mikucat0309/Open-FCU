package at.mikuc.openfcu.course.search.options

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CreditExtraOption(@Contextual var value: Int?): ExtraOptions() {
}