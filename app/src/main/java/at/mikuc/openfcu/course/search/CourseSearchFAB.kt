package at.mikuc.openfcu.course.search

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.mikuc.openfcu.course.CourseGraph

@Composable
fun CourseSearchFAB(ctrl: NavHostController, viewModel: CourseSearchViewModel = hiltViewModel()) {
    FloatingActionButton(
        onClick = {
            viewModel.search()
            ctrl.navigate(CourseGraph.Result.route) {
                popUpTo(CourseGraph.Search.route)
            }
        }
    ) {
        Icon(Icons.Outlined.Search, "Search")
    }
}
