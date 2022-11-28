package at.mikuc.openfcu.course.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CoursePreCourseView(viewModel: CourseDetailViewModel = getActivityViewModel()) {
    Column {
        PureCoursePreCourseView(viewModel.preCourses)
    }
}

@Composable
fun PureCoursePreCourseView(preCourses: List<PreCourse>) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (preCourses.isNotEmpty()) {
            preCourses.forEach {
                ListItem(it.name, it.description)
            }
        } else {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("無前置課程")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
fun PureCoursePreCoursePreview() {
    MixMaterialTheme {
        PureCoursePreCourseView(
            listOf(
                PreCourse("Lorem ipsum", "Lorem ipsum"),
                PreCourse("Lorem ipsum", "Lorem ipsum"),
            ),
        )
    }
}
