package at.mikuc.openfcu.timetable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.FcuRepository
import at.mikuc.openfcu.setting.UserPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TimetableViewModel(
    private val pref: UserPreferenceRepository,
    private val ttRepo: FcuRepository,
) : ViewModel(), KoinComponent {

    var state by mutableStateOf(TimetableUiState(emptyList()))

    fun fetchTimetable(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val cred = pref.getCredential().nonBlanked() ?: return@launch
            val list = ttRepo.fetchTimetable(cred) ?: return@launch
            state = state.copy(sections = list)
        }
    }
}
