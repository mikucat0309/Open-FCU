package at.mikuc.openfcu.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
) : ViewModel() {

    var state by mutableStateOf(SettingUiState("", ""))
    private val _event = MutableStateFlow(SettingEvent())
    val event: StateFlow<SettingEvent> = _event

    init {
        runBlocking {
            withTimeout(500) {
                state = SettingUiState(
                    pref.get(KEY_ID) ?: "",
                    pref.get(KEY_PASSWORD) ?: ""
                )
            }
        }
    }

    override fun onCleared() {
        saveConfig()
        super.onCleared()
    }

    fun saveConfig() {
        // TODO("check ID password")
        viewModelScope.launch {
            pref.set(KEY_ID, state.id)
            pref.set(KEY_PASSWORD, state.password)
        }
        _event.value = event.value.copy(toastMessage = "已儲存")
    }

    fun eventDone() {
        _event.value = SettingEvent()
    }
}
