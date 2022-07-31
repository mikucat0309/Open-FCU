package at.mikuc.openfcu.timetable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.setting.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TimetableUiState(
    val sections: List<Section>,
)

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
    private val ttRepo: FcuTimetableRepository,
) : ViewModel() {

    var state by mutableStateOf(TimetableUiState(emptyList()))

    fun fetchTimetable(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val cred = pref.getCredential() ?: return@launch
            val list = ttRepo.fetchTimetable(cred) ?: return@launch
            state = state.copy(sections = list)
        }
    }
}
