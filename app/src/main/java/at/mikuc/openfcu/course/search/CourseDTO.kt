package at.mikuc.openfcu.course.search

import android.util.Log
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.Opener
import at.mikuc.openfcu.course.Period
import kotlin.math.roundToInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RawCoursesDTO(val d: String) {
    fun toCourses(): List<Course> {
        val dto = Json.decodeFromString<CoursesDTO>(d)
        Log.d(TAG, Json.encodeToString(dto))
        return dto.items.map { courseDTO ->
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
                Log.d(TAG, "Course: $name Code: $code Class ID: $classId")
                Course(
                    name = name,
                    id = id,
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

val str2day = mapOf(
    "一" to 1,
    "二" to 2,
    "三" to 3,
    "四" to 4,
    "五" to 5,
    "六" to 6,
    "日" to 7,
)
