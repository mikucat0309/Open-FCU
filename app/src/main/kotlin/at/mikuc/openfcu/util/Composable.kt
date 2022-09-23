package at.mikuc.openfcu.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.getViewModel

@Composable
inline fun <reified T : ViewModel> getActivityViewModel(): T =
    getViewModel(owner = LocalContext.current.getActivity()!!)
