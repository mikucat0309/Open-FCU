package at.mikuc.openfcu.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import at.mikuc.openfcu.viewmodel.CourseSearchViewModel

@Composable
fun CourseSearchResultView(viewModel: CourseSearchViewModel) {
    LazyColumn {
        items(viewModel.result) { course ->
            Text(course.name)
        }
    }
}
