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

class QrcodeViewModel(
    private val pref: UserPreferenceRepository,
    private val repo: FcuRepository,
) : ViewModel() {

    var hexStr by mutableStateOf<String?>(null)
        private set

    fun fetchQrcode(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val credential = pref.getCredential().nonBlanked() ?: return@launch
            val hexStr2 = repo.fetchQrcode(credential) ?: return@launch
            hexStr = hexStr2
        }
    }
}
