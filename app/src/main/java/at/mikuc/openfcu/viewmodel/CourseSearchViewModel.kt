package at.mikuc.openfcu.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.data.Course
import at.mikuc.openfcu.data.RawCoursesDTO
import at.mikuc.openfcu.data.SearchFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CourseSearchViewModel @Inject constructor() : ViewModel() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { coerceInputValues = true })
        }
    }

    var state by mutableStateOf(SearchFilter(111, 1))
        private set
    var result: List<Course> by mutableStateOf(emptyList())
        private set

    fun update(new: SearchFilter) {
        state = new
    }

    fun search() {
        val filter = state.copy()
        val preFilter = filter.toDTO()
        Log.d(TAG, Json.encodeToString(preFilter))
        viewModelScope.launch {
            val response: RawCoursesDTO =
                client.post("https://coursesearch04.fcu.edu.tw/Service/Search.asmx/GetType2Result") {
                    contentType(ContentType.Application.Json)
                    setBody(preFilter)
                }.body()
            result = response.toCourses()
                .postFilter(filter)
        }
    }

    private fun List<Course>.postFilter(filter: SearchFilter): List<Course> {
        var list = this.distinctBy { Pair(it.code, it.name) }.sortedBy { it.code }
        return filter.run {
            if (sections.isNotEmpty() && sections.size >= 2) {
                list = list.filter { course ->
                    course.periods.map { it.range }.flatten().containsAll(sections)
                }
            }
            if (location.isNotBlank()) {
                list = list.filter { course ->
                    course.periods.any { period -> location in period.location }
                }
            }
            if (credit != null) {
                list = list.filter { it.credit == credit }
            }
            if (openerName.isNotBlank()) {
                list = list.filter { openerName in it.opener.name }
            }
            if (openNum != null) {
                list = list.filter { it.openNum == openNum }
            }
            list
        }
    }
}
