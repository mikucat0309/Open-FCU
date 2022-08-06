package at.mikuc.openfcu.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.openfcu.setting.UserPreferencesRepository.Companion.KEY_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
) : ViewModel() {

    var state by mutableStateOf(SettingUiState("", ""))

    init {
        viewModelScope.launch {
            state = SettingUiState(
                pref.get(KEY_ID) ?: "",
                pref.get(KEY_PASSWORD) ?: ""
            )
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
    }
}
