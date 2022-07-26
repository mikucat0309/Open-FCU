package at.mikuc.openfcu.course.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CourseSearchFAB(onClick: () -> Unit) {
    Box(Modifier.padding(16.dp)) {
        FloatingActionButton(onClick = onClick, modifier = Modifier.size(56.dp)) {
            Icon(Icons.Outlined.Search, "Search")
        }
    }
}
