package at.mikuc.openfcu.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import at.mikuc.openfcu.TAG
import at.mikuc.openfcu.data.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class SearchFilter(
    val year: Int,
    val semester: Int,
    val name: String = "",
    val code: Int? = null,
    val teacher: String = "",
    val day: Int? = null,
    val sections: Set<Int> = emptySet(),
    val place: String = "",
    val credit: Int? = null,
    val openerName: String = "",
    val openNum: Int? = null,
)

@HiltViewModel
class CourseSearchViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(SearchFilter(111, 1))
        private set
    var result: List<Course> by mutableStateOf(emptyList())
        private set

    fun update(new: SearchFilter) {
        state = new
    }

    fun search() {
        Log.w(TAG, "search")
    }
}
