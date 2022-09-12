package at.mikuc.openfcu.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import at.mikuc.openfcu.RootGraph

@Composable
fun NavHostController.currentRoute() = currentBackStackEntryAsState().value?.destination?.route

@Composable
fun NavHostController.currentGraph() = RootGraph.fromRoute(currentRoute())
