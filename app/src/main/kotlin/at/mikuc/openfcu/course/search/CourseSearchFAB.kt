package at.mikuc.openfcu.course.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun CourseSearchFAB(onClick: () -> Unit) {
    Box(Modifier.size(48.dp)) {
        FloatingActionButton(onClick = onClick) {
            Icon(Icons.Outlined.Search, "Search")
        }
    }
}
