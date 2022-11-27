package at.mikuc.openfcu.course

import at.mikuc.openfcu.util.IntRangeSerializer
import at.mikuc.openfcu.util.str2day
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.roundToInt

data class Course(
    val name: String,
    val id: String,
    val fullID: String,
    val code: Int,
    val teacher: String,
    val periods: List<Period>,
    val credit: Int,
    val opener: Opener,
    val openNum: Int,
    val acceptNum: Int,
    val remark: String?,
) {
    companion object {
        fun fromDTO(year: Int, semester: Int, json: JsonObject): List<Course> {
            val d = json["d"]!!.jsonPrimitive.content
            val coursesDTO = Json.decodeFromString<CoursesDTO>(d)
            return coursesDTO.items.map { courseDTO ->
                courseDTO.run {
                    var periods = Regex("""\((.)\)(\d{2}) +(\S+)""")
                        .findAll(period)
                        .map {
                            val (week, st, loc) = it.destructured
                            Period(str2day[week] ?: 0, IntRange(st.toInt(), st.toInt()), loc)
                        }
                    periods += Regex("""\((.)\)(\d{2})-(\d{2}) +(\S+)""")
                        .findAll(period)
                        .map {
                            val (week, st, ed, loc) = it.destructured
                            Period(str2day[week] ?: 0, IntRange(st.toInt(), ed.toInt()), loc)
                        }
                    var teacher = period.substringAfterLast(" ")
                        .trim(',').replace(',', '、')
                    if (periods.map { it.location }.any { loc -> teacher in loc })
                        teacher = ""
                    Course(
                        name = name,
                        id = id,
                        fullID = "$year$semester$classId$oldId$duplicate",
                        code = code.toInt(),
                        teacher = teacher,
                        periods = periods.toList(),
                        credit = credit.roundToInt(),
                        opener = Opener(
                            name = className,
                            academyId = classId.substring(0, 2),
                            departId = classId.substring(2, 4),
                            idk = classId[4],
                            grade = classId[5],
                            clazz = classId[6],
                        ),
                        openNum = openNum.roundToInt(),
                        acceptNum = acceptNum.roundToInt(),
                        remark = remark
                    )
                }
            }
        }
    }
}

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

@Serializable
data class Period(
    val day: Int,
    @Serializable(with = IntRangeSerializer::class)
    val range: IntRange,
    val location: String,
) {
    companion object {
        fun parse(period: String): List<Period> {
            val periods = Regex("""\((.)\)(\d{2}) +(\S+)""")
                .findAll(period)
                .map {
                    val (week, st, loc) = it.destructured
                    Period(str2day[week] ?: 0, IntRange(st.toInt() - 1, st.toInt() - 1), loc)
                }.toMutableList()
            periods += Regex("""\((.)\)(\d{2})-(\d{2}) +(\S+)""")
                .findAll(period)
                .map {
                    val (week, st, ed, loc) = it.destructured
                    Period(str2day[week] ?: 0, IntRange(st.toInt() - 1, ed.toInt() - 1), loc)
                }
            var teacher = period.substringAfterLast(" ").trim(',').replace(',', '、')
            if (periods.map { it.location }.any { loc -> teacher in loc })
                teacher = ""
            return periods
        }
    }
}

@Serializable
data class CoursesDTO(
    val message: String?,
    val total: Int,
    val items: List<CourseDTO>,
)

@Serializable
data class CourseDTO(
    @SerialName("scr_selcode") val code: String,
    @SerialName("sub_id3") val id: String,
    @SerialName("sub_name") val name: String,
    @SerialName("scr_credit") val credit: Double,
    @SerialName("scj_scr_mso") val scjScrMso: String,
    @SerialName("scr_examid") val examId: String,
    @SerialName("scr_examfn") val examFn: String,
    @SerialName("scr_exambf") val examBf: String,
    @SerialName("cls_name") val className: String,
    @SerialName("scr_period") val period: String,
    @SerialName("scr_precnt") val openNum: Double,
    @SerialName("scr_acptcnt") val acceptNum: Double,
    @SerialName("scr_remarks") val remark: String?,
    @SerialName("unt_ls") val unitLs: Int,
    @SerialName("cls_id") val classId: String,
    @SerialName("sub_id") val oldId: String,
    @SerialName("scr_dup") val duplicate: String,
)
