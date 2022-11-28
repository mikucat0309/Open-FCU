package at.mikuc.openfcu.course.search.options

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class DayExtraOption(@Contextual var value: Int?) : ExtraOptions() {
}
