package at.mikuc.openfcu.course.detail

import kotlinx.serialization.Serializable

@Serializable
data class TeachingProgress(
    val week: String,
    val abstract: String,
    val assessment: String,
    val note: String,
)
