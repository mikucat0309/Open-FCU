package at.mikuc.fcuassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.fcuassistant.repository.UserPreferencesRepository
import at.mikuc.fcuassistant.repository.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.fcuassistant.repository.UserPreferencesRepository.Companion.KEY_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: UserPreferencesRepository
) : ViewModel() {

    var id by mutableStateOf(TextFieldValue(""))
        private set
    var password by mutableStateOf(TextFieldValue(""))
        private set

    val onIDChange: (TextFieldValue) -> Unit = { id = it }
    val onPasswordChange: (TextFieldValue) -> Unit = { password = it }

    init {
        viewModelScope.launch {
            id = TextFieldValue(pref.get(KEY_ID, ""))
            password = TextFieldValue(pref.get(KEY_PASSWORD, ""))
        }
    }

    override fun onCleared() {
        saveConfig()
        super.onCleared()
    }

    fun saveConfig() {
        // TODO("check ID password")
        viewModelScope.launch {
            pref.set(KEY_ID, id.text)
            pref.set(KEY_PASSWORD, password.text)
        }
    }
}