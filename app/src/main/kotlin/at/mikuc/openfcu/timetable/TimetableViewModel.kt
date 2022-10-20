package at.mikuc.openfcu.timetable

import android.graphics.Bitmap
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
    private val repo: FcuRepository,
) : ViewModel(), KoinComponent {

    var sections by mutableStateOf(emptyList<Section>())
        private set
    var isClocked by mutableStateOf(false)
    var captcha by mutableStateOf<Bitmap?>(null)

    init {
        fetchTimetable()
    }

    fun fetchTimetable(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val cred = pref.getCredential().nonBlanked() ?: return@launch
            val list = repo.fetchTimetable(cred) ?: return@launch
            sections = list
        }
    }

    fun fetchClockStatus() {

    }

    fun clock() {

    }
}
