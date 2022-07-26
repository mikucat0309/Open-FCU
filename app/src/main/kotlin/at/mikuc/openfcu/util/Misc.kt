package at.mikuc.openfcu.util

import kotlinx.serialization.Serializable

val day2str = mapOf(
    0 to "日",
    1 to "一",
    2 to "二",
    3 to "三",
    4 to "四",
    5 to "五",
    6 to "六",
    7 to "日",
)

val str2day = mapOf(
    "一" to 1,
    "二" to 2,
    "三" to 3,
    "四" to 4,
    "五" to 5,
    "六" to 6,
    "日" to 7,
)

val section2str = listOf(
    "",
    " 8:10 -  9:00",
    " 9:10 - 10:00",
    "10:10 - 11:00",
    "11:10 - 12:00",
    "12:10 - 13:00",
    "13:10 - 14:00",
    "14:10 - 15:00",
    "15:10 - 16:00",
    "16:10 - 17:00",
    "17:10 - 18:00",
    "18:30 - 19:20",
    "19:25 - 20:15",
    "20:25 - 21:15",
    "21:20 - 22:10",
)

val startTime = listOf(
    " 8:10",
    " 9:10",
    "10:10",
    "11:10",
    "12:10",
    "13:10",
    "14:10",
    "15:10",
    "16:10",
    "17:10",
    "18:30",
    "19:25",
    "20:25",
    "21:20",
)

val endTime = listOf(
    " 9:00",
    "10:00",
    "11:00",
    "12:00",
    "13:00",
    "14:00",
    "15:00",
    "16:00",
    "17:00",
    "18:00",
    "19:20",
    "20:15",
    "21:15",
    "22:10",
)

fun IntRange.toTimeRangeString(): String {
    return startTime[first] + " - " + endTime[last]
}

@Serializable
data class MagicDTO(val d: String)

@Serializable
data class MagicDTO2<T>(val d: List<T>)
