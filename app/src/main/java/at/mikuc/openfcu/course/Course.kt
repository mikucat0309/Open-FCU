package at.mikuc.openfcu.course

data class Course(
    val name: String,
    val id: String,
    val code: Int,
    val teacher: String,
    val periods: List<Period>,
    val credit: Int,
    val opener: Opener,
    val openNum: Int,
    val acceptNum: Int,
    val remark: String?,
)

// CE 電資學院
// 07 資訊系
// 1 IDK what's this
// 3 年級
// 1 班級
data class Opener(
    val name: String,
    val academyId: String,
    val departId: String,
    val idk: Char,
    val grade: Char,
    val clazz: Char,
)

data class Period(
    val day: Int,
    val range: IntRange,
    val location: String,
)
