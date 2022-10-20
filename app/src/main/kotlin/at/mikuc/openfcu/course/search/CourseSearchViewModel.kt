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

    var result: List<Course> by mutableStateOf(emptyList())
        private set

    fun search(filter: SearchFilter, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        Log.d(TAG, filter.toString())
        viewModelScope.launch(dispatcher) {
            repo.search(filter)?.let { result = it.postFilter(filter) }
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
