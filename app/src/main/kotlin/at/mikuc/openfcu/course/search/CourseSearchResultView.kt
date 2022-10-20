package at.mikuc.openfcu.course.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.Opener
import at.mikuc.openfcu.course.Period
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.day2str
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CourseSearchResultView(viewModel: CourseSearchViewModel = getActivityViewModel()) {
    CourseLazyColumnView(courses = viewModel.result)
}

@Composable
private fun CourseLazyColumnView(courses: List<Course>) {
    SelectionContainer {
        LazyColumn(
            Modifier
                .background(MaterialTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { CourseCard(it) }
        }
    }
}

@Composable
private fun CourseCard(course: Course) {
    Card(
        elevation = 2.dp,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CodeField(course = course)
                CourseNameField(course)
            }
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OpenerField(course = course, Modifier.width(70.dp))
                TeacherField(course, Modifier.weight(1.0f))
                PeriodField(course, Modifier.width(130.dp))
            }
        }
    }
}

@Composable
private fun CodeField(course: Course, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = course.code.toString().padStart(4, '0'),
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun OpenerField(course: Course, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = course.opener.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun CourseNameField(course: Course, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = course.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun TeacherField(course: Course, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = course.teacher,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun PeriodField(course: Course, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val text = course.periods.take(2).joinToString("\n") { period ->
            val rangeStr =
                if (period.range.first == period.range.last) period.range.first.toString()
                else "${period.range.first}-${period.range.last}"
            "(${day2str[period.day] ?: "N/A"}) $rangeStr"
        }
        Text(
            text = text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun CourseSearchResultPreview() {
    val c = Course(
        name = "中文思辨與表達(一)",
        id = "ID",
        code = 1,
        teacher = "王小明、王中明",
        periods = listOf(
            Period(4, (9..14), "布宜諾斯艾利斯"),
            Period(5, (12..14), "躲貓貓社辦")
        ),
        credit = 3,
        opener = Opener(
            "美國加州舊金山州立大學",
            "acaID",
            "DEPART_ID",
            'A',
            'B',
            'C'
        ),
        openNum = 70,
        acceptNum = 70,
        remark = "This is a REMARK This is a REMARK This is a REMARK"
    )
    MixMaterialTheme {
        Surface {
            CourseLazyColumnView(
                courses = listOf(c, c)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380)
@Composable
fun CourseSearchResultDarkPreview() {
    val c = Course(
        name = "中文思辨與表達中文思辨與表達(一)",
        id = "ID",
        code = 1,
        teacher = "王小明、王中明",
        periods = listOf(
            Period(4, (9..14), "布宜諾斯艾利斯是個很長的地名"),
            Period(5, (12..14), "躲貓貓社辦")
        ),
        credit = 3,
        opener = Opener(
            "美國加州舊金山州立大學資訊工程雙學士學位學程",
            "acaID",
            "DEPART_ID",
            'A',
            'B',
            'C'
        ),
        openNum = 70,
        acceptNum = 70,
        remark = "This is a REMARK This is a REMARK This is a REMARK"
    )
    MixMaterialTheme(darkTheme = true) {
        Surface {
            CourseLazyColumnView(
                courses = listOf(c, c)
            )
        }
    }
}
