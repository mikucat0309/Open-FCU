package at.mikuc.openfcu.course.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.FcuRepository
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.course.Course
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CourseSearchViewModel(
    private val repo: FcuRepository
) : ViewModel(), KoinComponent {

    var state by mutableStateOf(SearchFilter(111, 1))
        private set
    var result: List<Course> by mutableStateOf(emptyList())
        private set

    fun updateFilter(value: SearchFilter) {
        state = value
    }

    fun updateYear(value: Int) {
        state = state.copy(year = value)
    }

    fun updateSemester(value: Int) {
        state = state.copy(semester = value)
    }

    fun updateCourseName(value: String) {
        state = state.copy(name = value)
    }

    fun updateCode(value: Int?) {
        state = state.copy(code = value)
    }

    fun updateTeacher(value: String) {
        state = state.copy(teacher = value)
    }

    fun updateDay(value: Int?) {
        state = state.copy(day = value)
    }

    fun updateSections(value: Set<Int>) {
        state = state.copy(sections = value)
    }

    fun updateLocation(value: String) {
        state = state.copy(location = value)
    }

    fun updateCredit(value: Int?) {
        state = state.copy(credit = value)
    }

    fun updateOpenerName(value: String) {
        state = state.copy(openerName = value)
    }

    fun updateOpenNum(value: Int?) {
        state = state.copy(openNum = value)
    }

    fun search(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        val filter = state.copy()
        Log.d(TAG, filter.toString())
        if (!isValidFilter(filter)) return
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
