package at.mikuc.openfcu.course.detail

import kotlinx.serialization.Serializable

@Serializable
data class PreCourse(
    val name: String,
    val description: String,
)
