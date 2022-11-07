package at.mikuc.openfcu.course.detail

import at.mikuc.openfcu.course.Period
import kotlinx.serialization.Serializable

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
