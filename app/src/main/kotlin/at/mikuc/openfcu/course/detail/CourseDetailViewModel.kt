package at.mikuc.openfcu.course.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CourseDetailViewModel(
    private val repo: CourseDetailRepository
) : ViewModel(), KoinComponent {

    var fullID by mutableStateOf("")
        private set
    var info by mutableStateOf(CourseInfo("", "", "", "", 0.0, emptyList()))
        private set
    var description by mutableStateOf("")
        private set
    var preCourses by mutableStateOf(emptyList<PreCourse>())
        private set
    var assessment by mutableStateOf("")
        private set
    var teachingMaterials by mutableStateOf(emptyList<TeachingMaterial>())
        private set
    var teachingProgresses by mutableStateOf(emptyList<TeachingProgress>())
        private set

    fun updateFullID(fullID: String) {
        this.fullID = fullID
        fetchCourseDetail()
    }

    private fun fetchCourseDetail(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        require(fullID.length == 19) { "Invalid length of course full ID" }
        viewModelScope.launch(dispatcher) {
            val hashID = repo.getHashID(fullID)
            launch { info = repo.getInfo(hashID) }
            launch { description = repo.getDescription(hashID) }
            launch { preCourses = repo.getPreCourses(hashID) }
            launch { assessment = repo.getAssessment(hashID) }
            launch { teachingMaterials = repo.getTeachingMaterials(hashID) }
            launch { teachingProgresses = repo.getTeachingProgresses(hashID) }
        }
    }
}
