package at.mikuc.openfcu.course.detail

import android.util.Log
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.course.Period
import at.mikuc.openfcu.course.detail.dto.AssessmentDTO
import at.mikuc.openfcu.course.detail.dto.CourseDescDTO
import at.mikuc.openfcu.course.detail.dto.CourseInfoDTO
import at.mikuc.openfcu.course.detail.dto.HashIDDTO
import at.mikuc.openfcu.course.detail.dto.PreCourseDTO
import at.mikuc.openfcu.course.detail.dto.ReferenceDTO
import at.mikuc.openfcu.course.detail.dto.SoftwareDTO
import at.mikuc.openfcu.course.detail.dto.TeachingProgressDTO
import at.mikuc.openfcu.course.detail.dto.TextbookDTO
import at.mikuc.openfcu.util.MagicDTO
import at.mikuc.openfcu.util.MagicDTO2
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CourseDetailRepository(private val client: HttpClient) : KoinComponent {

    private val json: Json by inject()

//    suspend fun getCourseDetail(fullID: String): CourseDetail {
//        require(fullID.length == 19) { "Invalid length of course full ID" }
//        val hashID = getHashID(fullID)
//        return coroutineScope {
//            val info = async { getInfo(hashID) }
//            val description = async { getDescription(hashID) }
//            val preCourses = async { getPreCourses(hashID) }
//            val assessment = async { getAssessment(hashID) }
//            val teachingMaterials = async { getTeachingMaterials(hashID) }
//            val teachingProgress = async { getTeachingProgress(hashID) }
//            CourseDetail()
//        }
//    }

    suspend fun getHashID(fullID: String): String {
        val html = client.get("https://service120-sds.fcu.edu.tw/W320104/W320104_stu_pre.aspx") {
            parameter("courseid", fullID)
            parameter("lang", "cht")
        }.bodyAsText()
        return Regex("""'course_id', '([0-9a-f]+)'""").find(html)!!.groupValues[1]
    }

    suspend fun getInfo(hashID: String): CourseInfo {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getCourseInfo") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        Log.w(TAG, d)
        return json.decodeFromString<List<CourseInfoDTO>>(d)[0].run {
            CourseInfo(
                hashID,
                sub_name,
                scr_teacher,
                cls_name,
                scr_credit,
                Period.parse(scr_period).first,
            )
        }
    }

    suspend fun getDescription(hashID: String): String {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getCourseDescr") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        return json.decodeFromString<List<CourseDescDTO>>(d)[0].describe_ch
    }

    suspend fun getPreCourses(hashID: String): List<PreCourse> {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getPreCourse") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        if (d.isBlank()) return emptyList()
        return json.decodeFromString<List<PreCourseDTO>>(d).map {
            PreCourse(it.sub_name, it.pre_describe)
        }
    }

    suspend fun getAssessment(hashID: String): String {
        return client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getEvoluationInfo") {
            contentType(ContentType.Application.Json)
            setBody(HashIDDTO(hashID))
        }.body<MagicDTO2<AssessmentDTO>>().d[0].tpa_score_new
    }

    suspend fun getTeachingMaterials(hashID: String): List<TeachingMaterial> {
        return getTextbook(hashID) + getReference(hashID) + getSoftware(hashID)
    }

    suspend fun getTextbook(hashID: String): List<TeachingMaterial> {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getTextbook") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        if (d.isBlank()) return emptyList()
        return json.decodeFromString<List<TextbookDTO>>(d).map {
            TeachingMaterial(
                it.tpb_book,
                listOf(
                    it.tpb_author,
                    it.tpb_publish,
                )
            )
        }
    }

    suspend fun getReference(hashID: String): List<TeachingMaterial> {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getReference") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        if (d.isBlank()) return emptyList()
        return json.decodeFromString<List<ReferenceDTO>>(d).map {
            TeachingMaterial(
                it.tpc_book,
                listOf(
                    it.tpc_author,
                    it.tpc_publish,
                )
            )
        }
    }

    suspend fun getSoftware(hashID: String): List<TeachingMaterial> {
        return client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getSoftWare") {
            contentType(ContentType.Application.Json)
            setBody(HashIDDTO(hashID))
        }.body<MagicDTO2<SoftwareDTO>>().d.map {
            TeachingMaterial(
                it.soft_name + " " + it.soft_version,
                listOf(
                    it.soft_memo
                )
            )
        }
    }

    suspend fun getTeachingProgresses(hashID: String): List<TeachingProgress> {
        val d =
            client.post("https://service120-sds.fcu.edu.tw/W320104/action/getdata.aspx/getTeachSchedule") {
                contentType(ContentType.Application.Json)
                setBody(HashIDDTO(hashID))
            }.body<MagicDTO>().d
        if (d.isBlank()) return emptyList()
        return json.decodeFromString<List<TeachingProgressDTO>>(d).map {
            TeachingProgress(
                it.tpd_week,
                it.tpd_content,
                it.tpd_exam,
                it.tpd_remark,
            )
        }
    }
}
