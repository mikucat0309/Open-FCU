package at.mikuc.openfcu.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import kotlin.time.Duration.Companion.milliseconds

class SettingViewModel(
    private val pref: UserPreferenceRepository
) : ViewModel(), KoinComponent {

    var state by mutableStateOf(Credential("", ""))
    private val _event = MutableStateFlow(SettingEvent())
    val event: StateFlow<SettingEvent> = _event

    init {
        runBlocking {
            withTimeout(500) {
                state = pref.getCredential()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        saveConfig()
    }

    fun saveConfig() {
        // TODO("check ID password")
        _event.value = event.value.copy(toastMessage = "已儲存")
        viewModelScope.launch {
            withTimeout(500.milliseconds) {
                pref.setCredential(state)
            }
        }
    }

    fun eventDone() {
        _event.value = SettingEvent()
    }
}
