package at.mikuc.openfcu.course.search

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun CourseSearchFAB(viewModel: CourseSearchViewModel = getActivityViewModel()) {
    val nav = LocalNavHostController.currentOrThrow
    FloatingActionButton(
        onClick = {
            viewModel.search()
            nav.navigate(CourseSearchResultViewDestination)
        }
    ) {
        Icon(Icons.Outlined.Search, "Search")
    }
}
