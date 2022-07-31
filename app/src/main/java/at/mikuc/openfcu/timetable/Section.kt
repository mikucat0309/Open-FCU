package at.mikuc.openfcu.timetable

data class Section(
    val method: Int?,
    val memo: String,
    val time: String,
    val location: String,
    val section: Int,
    val day: Int,
    val name: String,
)

