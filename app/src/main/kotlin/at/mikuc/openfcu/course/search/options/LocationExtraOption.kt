package at.mikuc.openfcu.course.search.options

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class LocationExtraOption(@Contextual var text: String): ExtraOptions() {
}