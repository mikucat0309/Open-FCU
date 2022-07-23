package at.mikuc.openfcu.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class SearchFilter(
    val year: Int,
    val semester: Int,
    val name: String = "",
    val code: Int? = null,
    val teacher: String = "",
    val day: Int? = null,
    val sections: Set<Int> = emptySet(),
    val location: String = "",
    val credit: Int? = null,
    val openerName: String = "",
    val openNum: Int? = null,
) {
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
        if (day != null || sections.isNotEmpty()) putJsonObject("weekPeriod") {
            put("enabled", true)
            put("week", day?.toString() ?: "*")
            put("period", if (sections.size == 1) sections.first().toString() else "*")
        }
    }
}