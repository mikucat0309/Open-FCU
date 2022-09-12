package at.mikuc.openfcu.course.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.course.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@HiltViewModel
class CourseSearchViewModel @Inject constructor(
    private val repo: FcuCourseSearchRepository,
) : ViewModel() {

    var state by mutableStateOf(SearchFilter(111, 1))
    var result: List<Course> by mutableStateOf(emptyList())
        private set

    fun search(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        val filter = state.copy()
        if (!isValidFilter(filter)) {
            return
        }
        Log.d(TAG, Json.encodeToString(filter))
        viewModelScope.launch(dispatcher) {
            repo.search(filter)?.let { result = it.postFilter(filter) }
        }
    }

    private fun isValidFilter(filter: SearchFilter): Boolean {
        return filter.run {
            name.isNotBlank() ||
                teacher.isNotBlank() ||
                code != null ||
                day != null ||
                sections.isNotEmpty()
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
