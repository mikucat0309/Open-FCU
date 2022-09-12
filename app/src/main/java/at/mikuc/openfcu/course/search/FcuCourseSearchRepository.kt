package at.mikuc.openfcu.course.search

import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.util.catchNetworkException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Inject
import kotlinx.serialization.json.Json

const val COURSE_SEARCH_URL = "https://coursesearch04.fcu.edu.tw/Service/Search.asmx/GetType2Result"

class FcuCourseSearchRepository @Inject constructor() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    suspend fun search(filter: SearchFilter): List<Course>? {
        return catchNetworkException {
            client.post(COURSE_SEARCH_URL) {
                contentType(ContentType.Application.Json)
                setBody(filter.toDTO())
            }.body<RawCoursesDTO>().toCourses()
        }
    }
}
