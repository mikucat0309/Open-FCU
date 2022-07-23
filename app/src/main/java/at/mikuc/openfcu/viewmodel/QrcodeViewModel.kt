package at.mikuc.openfcu.viewmodel

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.mikuc.openfcu.repository.FcuQrcodeRepository
import at.mikuc.openfcu.repository.UserPreferencesRepository
import at.mikuc.openfcu.repository.UserPreferencesRepository.Companion.KEY_ID
import at.mikuc.openfcu.repository.UserPreferencesRepository.Companion.KEY_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.QRCode.Companion.DEFAULT_CELL_SIZE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QrcodeUiState(
    val bitmap: Bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).apply {
        eraseColor(Color.GRAY)
    },
)

@HiltViewModel
class QrcodeViewModel @Inject constructor(
    private val pref: UserPreferencesRepository,
    private val repo: FcuQrcodeRepository,
) : ViewModel() {

    var state by mutableStateOf(QrcodeUiState())

    init {
        fetchQrcode()
    }

    fun fetchQrcode(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        viewModelScope.launch(dispatcher) {
            val id = pref.get(KEY_ID) ?: return@launch
            val password = pref.get(KEY_PASSWORD) ?: return@launch
            val hexStr = repo.fetchQrcode(id, password) ?: return@launch
            val bitmap = QRCode(hexStr).render(margin = DEFAULT_CELL_SIZE).nativeImage() as Bitmap
            state = state.copy(bitmap = bitmap)
        }
    }
}
