package at.mikuc.openfcu.main

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalScaffoldState: ProvidableCompositionLocal<ScaffoldState?> =
    staticCompositionLocalOf { null }
