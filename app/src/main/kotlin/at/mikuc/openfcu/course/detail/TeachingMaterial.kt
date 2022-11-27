package at.mikuc.openfcu.course.detail

import kotlinx.serialization.Serializable

@Serializable
data class TeachingMaterial(
    val name: String,
    val desc: List<String>,
)
