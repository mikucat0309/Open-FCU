package at.mikuc.openfcu.course.detail

import kotlinx.serialization.Serializable
import at.mikuc.openfcu.course.Period

// TODO change naming
@Serializable
data class CourseInfo(
    val hashID: String,
    val subName: String,
    val teacher: String,
    val clsName: String,
    val scrCredit: Double,
    val periods: List<Period>,
)
