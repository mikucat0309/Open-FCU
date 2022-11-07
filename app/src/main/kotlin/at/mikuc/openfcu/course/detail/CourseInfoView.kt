package at.mikuc.openfcu.course.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.course.Period
import at.mikuc.openfcu.theme.MaterialTheme3
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.day2str
import at.mikuc.openfcu.util.getActivityViewModel
import at.mikuc.openfcu.util.toTimeRangeString
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun CourseInfoView(viewModel: CourseDetailViewModel = getActivityViewModel()) {
    PureCourseInfoView(viewModel.info, viewModel.description)
}

@Composable
fun PureCourseInfoView(info: CourseInfo, description: String) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ListItem("課程名稱", info.subName)
        ListItem("教師名稱", info.teacher)
        Row {
            ListItem("開課單位", info.clsName, Modifier.weight(1.0f))
            ListItem("學分數", info.scrCredit.toString(), Modifier.weight(1.0f))
        }
        Column(
            Modifier.padding(vertical = 8.dp)
        ) {
            Row(
                Modifier.height(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "上課時間與地點",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme3.colorScheme.onSurface
                )
            }
            Column(
                Modifier.height(24.dp)
            ) {
                info.periods.forEach {
                    PeriodRow(it)
                }
            }
        }
        Column(
            Modifier.padding(vertical = 8.dp)
        ) {
            ListItem("課程描述", description)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
fun PureCourseInfoPreview() {
    MixMaterialTheme {
        PureCourseInfoView(
            info = CourseInfo(
                "1234567890abcdef123",
                "Lorem ipsum",
                "Lorem ipsum",
                "Lorem ipsum",
                1.0,
                listOf(
                    Period(2, 2..3, "Lorem ipsum"),
                    Period(2, 4..5, "Lorem ipsum"),
                ),
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                preCourses = listOf(
//                    PreCourse("Lorem ipsum", "Lorem ipsum"),
//                    PreCourse("Lorem ipsum", "Lorem ipsum"),
//                ),
//                assessment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                teachingMaterials = listOf(
//                    TeachingMaterial("Lorem ipsum", listOf("Lorem ipsum")),
//                    TeachingMaterial("Lorem ipsum", listOf("Lorem ipsum"))
//                ),
//                teachingProgresses = listOf(
//                    TeachingProgress("1-2", "Lorem ipsum", "Lorem ipsum", "Lorem ipsum"),
//                    TeachingProgress("1-2", "Lorem ipsum", "Lorem ipsum", "Lorem ipsum"),
//                )
        )
    }
}

@Composable
private fun PeriodRow(period: Period) {
    Row(
        Modifier.heightIn(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "星期" + day2str[period.day],
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme3.colorScheme.onSurfaceVariant
        )
        Text(
            period.range.toTimeRangeString(),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme3.colorScheme.onSurfaceVariant
        )
        Text(
            period.location,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme3.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier.padding(vertical = 8.dp)
) {
    Column(
        modifier,
    ) {
        Row(
            Modifier.heightIn(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme3.colorScheme.onSurface
            )
        }
        Row(
            Modifier.heightIn(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                subtitle,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme3.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 328)
@Composable
fun ListItemPreview() {
    ListItem("Lorem ipsum", "Lorem ipsum dolor sit amet")
}
