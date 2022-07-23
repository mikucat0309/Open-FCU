package at.mikuc.openfcu.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavHostController.currentRoute() = currentBackStackEntryAsState().value?.destination?.route