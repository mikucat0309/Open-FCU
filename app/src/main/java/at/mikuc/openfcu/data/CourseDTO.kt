package at.mikuc.openfcu.data

import android.util.Log
import at.mikuc.openfcu.TAG
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

private val json = Json { prettyPrint = true }

@Serializable
data class RawCoursesDTO(
    val d: String,
) {
    fun toCourses(): List<Course> {
        val dto = Json.decodeFromString<CoursesDTO>(this.d)
        Log.d(TAG, json.encodeToString(dto))
        return dto.items.map { courseDTO ->
            courseDTO.run {
                var periods = Regex("""\((.)\)(\d{2}) +([^\s]+)""")
                    .findAll(scr_period)
                    .map {
                        val (week, st, loc) = it.destructured
                        Period(chineseMapping[week]!!, IntRange(st.toInt(), st.toInt()), loc)
                    }
                periods += Regex("""\((.)\)(\d{2})-(\d{2}) +([^\s]+)""")
                    .findAll(scr_period)
                    .map {
                        val (week, st, ed, loc) = it.destructured
                        Period(chineseMapping[week]!!, IntRange(st.toInt(), ed.toInt()), loc)
                    }
                var teacher = scr_period.substringAfterLast(" ")
                    .trim(',').replace(',', '、')
                if (periods.map { it.location }.any { loc -> teacher in loc })
                    teacher = ""
                Log.d(TAG, "Course: $sub_name Code: $scr_selcode Class ID: $cls_id")
                Course(
                    name = sub_name,
                    id = sub_id3,
                    code = scr_selcode.toInt(),
                    teacher = teacher,
                    periods = periods.toList(),
                    credit = scr_credit.roundToInt(),
                    opener = Opener(
                        name = cls_name,
                        academyId = cls_id.substring(0, 2),
                        departId = cls_id.substring(2, 4),
                        idk = cls_id[4],
                        grade = cls_id[5],
                        clazz = cls_id[6],
                    ),
                    openNum = scr_precnt.roundToInt(),
                    acceptNum = scr_acptcnt.roundToInt(),
                    remark = scr_remarks
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
    val scr_selcode: String,
    val sub_id3: String,
    val sub_name: String,
    val scr_credit: Double,
    val scj_scr_mso: String,
    val scr_examid: String,
    val scr_examfn: String,
    val scr_exambf: String,
    val cls_name: String,
    val scr_period: String,
    val scr_precnt: Double,
    val scr_acptcnt: Double,
    val scr_remarks: String?,
    val unt_ls: Int,
    val cls_id: String,
    val sub_id: String,
    val scr_dup: String,
)

val chineseMapping = mapOf(
    "一" to 1,
    "二" to 2,
    "三" to 3,
    "四" to 4,
    "五" to 5,
    "六" to 6,
    "日" to 7,
)
