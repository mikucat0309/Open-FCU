package at.mikuc.openfcu.course.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.course.Course
import at.mikuc.openfcu.course.Opener
import at.mikuc.openfcu.course.Period

@Composable
fun CourseSearchResultView(viewModel: CourseSearchViewModel) {
    CourseLazyColumnView(courses = viewModel.result)
}

@Composable
private fun CourseLazyColumnView(courses: List<Course>) {
    SelectionContainer {
        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            items(courses) { CourseRow(it) }
        }
    }
}

@Composable
private fun CourseRow(course: Course) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CourseNameField(course, Modifier.weight(2f, fill = true))
        Spacer(Modifier.width(8.dp))
        TeacherField(course, Modifier.weight(1f, fill = true))
        Spacer(Modifier.width(8.dp))
        PeriodField(course, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CourseNameField(course: Course, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = course.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun TeacherField(course: Course, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = course.teacher,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun PeriodField(course: Course, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        course.periods.forEach { PeriodLine(it) }
    }
}

@Composable
private fun PeriodLine(period: Period) {
    val rangeStr =
        if (period.range.first == period.range.last) period.range.first.toString()
        else "${period.range.first}-${period.range.last}"
    Text(
        text = "(${day2str[period.day] ?: "N/A"}) $rangeStr",
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Clip,
        style = MaterialTheme.typography.body1
    )
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun CourseSearchResultPreview() {
    CourseLazyColumnView(
        courses = listOf(
            Course(
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
                    "OP_NAME",
                    "acaID",
                    "DEPART_ID",
                    'A',
                    'B',
                    'C'
                ),
                openNum = 70,
                acceptNum = 70,
                remark = "This is a REMARK This is a REMARK This is a REMARK This is a REMARK"
            )
        )
    )
}

val day2str = mapOf(
    1 to "一",
    2 to "二",
    3 to "三",
    4 to "四",
    5 to "五",
    6 to "六",
    7 to "日",
)
