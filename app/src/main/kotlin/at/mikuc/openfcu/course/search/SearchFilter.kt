package at.mikuc.openfcu.course.search

import at.mikuc.openfcu.course.search.options.CreditExtraOption
import at.mikuc.openfcu.course.search.options.ExtraOptions
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

@Serializable
data class SearchFilter(
    val year: Int,
    val semester: Int,
    val name: String = "",
    val code: Int? = null,
    val teacher: String = "",
    val day: Int? = null,
    val sections: Int? = null,
    val extraField: List<ExtraOptions>? = emptyList()
) {
    fun isValid(): Boolean {
        return name.isNotBlank() ||
                teacher.isNotBlank() ||
                code != null ||
                day != null ||
                sections != null
    }

    fun toDTO(): JsonObject {
        return buildJsonObject {
            putJsonObject("baseOptions") {
                put("lang", "cht")
                put("year", year)
                put("sms", semester)
            }
            putJsonObject("typeOptions") {
                putNameFilter()
                putTeacherFilter()
                putCodeFilter()
                putPeriodFilter()
            }
        }
    }

    private fun JsonObjectBuilder.putNameFilter() {
        if (name.isNotBlank()) putJsonObject("course") {
            put("enabled", true)
            put("value", name)
        }
    }

    private fun JsonObjectBuilder.putTeacherFilter() {
        if (teacher.isNotBlank()) putJsonObject("teacher") {
            put("enabled", true)
            put("value", teacher)
        }
    }

    private fun JsonObjectBuilder.putCodeFilter() {
        if (code != null) putJsonObject("code") {
            put("enabled", true)
            put("value", code.toString())
        }
    }

    private fun JsonObjectBuilder.putPeriodFilter() {
        if (day != null || sections != null) putJsonObject("weekPeriod") {
            put("enabled", true)
            put("week", day?.toString() ?: "*")
            put("period", sections?.toString() ?: "*")
        }
    }
}
