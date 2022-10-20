package at.mikuc.openfcu.util

import java.time.LocalDateTime
import java.time.LocalTime

private val durations = listOf(
    LocalTime.of(8, 10).rangeTo(LocalTime.of(9, 0)),
    LocalTime.of(9, 10).rangeTo(LocalTime.of(10, 0)),
    LocalTime.of(10, 10).rangeTo(LocalTime.of(11, 0)),
    LocalTime.of(11, 10).rangeTo(LocalTime.of(12, 0)),
    LocalTime.of(12, 10).rangeTo(LocalTime.of(13, 0)),
    LocalTime.of(13, 10).rangeTo(LocalTime.of(14, 0)),
    LocalTime.of(14, 10).rangeTo(LocalTime.of(15, 0)),
    LocalTime.of(15, 10).rangeTo(LocalTime.of(16, 0)),
    LocalTime.of(16, 10).rangeTo(LocalTime.of(17, 0)),
    LocalTime.of(17, 10).rangeTo(LocalTime.of(18, 0)),
    LocalTime.of(18, 30).rangeTo(LocalTime.of(19, 20)),
    LocalTime.of(19, 25).rangeTo(LocalTime.of(20, 15)),
    LocalTime.of(20, 25).rangeTo(LocalTime.of(21, 15)),
    LocalTime.of(21, 20).rangeTo(LocalTime.of(22, 10)),
)

val LocalDateTime.currentSection: Int?
    get() {
        val t = toLocalTime()
        durations.forEachIndexed { i, range -> if (t in range) return i }
        return null
    }

