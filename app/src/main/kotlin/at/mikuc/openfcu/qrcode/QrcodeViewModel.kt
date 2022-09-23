package at.mikuc.openfcu.qrcode

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

class QrcodeViewModel(
    private val pref: UserPreferenceRepository,
    private val repo: FcuRepository,
) : ViewModel(), KoinComponent {

    var state by mutableStateOf(QrcodeUiState())
        private set

    fun fetchQrcode(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val credential = pref.getCredential().nonBlanked() ?: return@launch
            val hexStr = repo.fetchQrcode(credential) ?: return@launch
            state = state.copy(hexStr = hexStr)
        }
    }
}
