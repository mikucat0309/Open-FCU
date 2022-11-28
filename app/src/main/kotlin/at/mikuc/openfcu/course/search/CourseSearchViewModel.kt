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
import at.mikuc.openfcu.course.search.options.CreditExtraOption
import at.mikuc.openfcu.course.search.options.DayExtraOption
import at.mikuc.openfcu.course.search.options.LocationExtraOption
import at.mikuc.openfcu.course.search.options.OpenerNameExtraOption
import at.mikuc.openfcu.course.search.options.SectionsExtraOption
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
            if (sections != null) {
                list = list.filter { course ->
                    course.periods.any { period -> sections in period.range }
                }
            }

            if (day != null) {
                list = list.filter { course -> course.periods.any { period -> day == period.day } }
            }

            extraField?.forEach { item ->
                when (item) {
                    is SectionsExtraOption -> {
                        list =
                            list.filter { course -> course.periods.any { period -> item.value in period.range } }
                    }
                    is LocationExtraOption -> {
                        list = list.filter { course ->
                            course.periods.any { period -> item.text in period.location }
                        }
                    }
                    is CreditExtraOption -> {
                        list = list.filter { it.credit == item.value }
                    }
                    is OpenerNameExtraOption -> {
                        list = list.filter { item.text in it.opener.name }
                    }
                    is DayExtraOption -> {
                        list =
                            list.filter { course -> course.periods.any { period -> item.value == period.day } }
                    }
                }
            }

//            if (location.isNotBlank()) {
//                list = list.filter { course ->
//                    course.periods.any { period -> location in period.location }
//                }
//            }
//            if (credit != null) {
//                list = list.filter { it.credit == credit }
//            }
//            if (openerName.isNotBlank()) {
//                list = list.filter { openerName in it.opener.name }
//            }
            // TODO: 開課人數記得加到 filter 和 UI 上
//            if (openNum != null) {
//                list = list.filter { it.openNum == openNum }
//            }
            list
        }
    }
}
