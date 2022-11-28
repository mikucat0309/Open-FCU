package at.mikuc.openfcu.course.search.options

import kotlinx.serialization.Contextual

data class SectionsExtraOption(@Contextual var value: Int?): ExtraOptions() {
}