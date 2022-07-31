package at.mikuc.openfcu.course

import at.mikuc.openfcu.COURSE_SEARCH_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Inject


class FcuCourseSearchRepository @Inject constructor() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    suspend fun search(filter: SearchFilter): List<Course> {
        return client.post(COURSE_SEARCH_URL) {
            contentType(ContentType.Application.Json)
            setBody(filter.toDTO())
        }.body<RawCoursesDTO>().toCourses()
    }
}