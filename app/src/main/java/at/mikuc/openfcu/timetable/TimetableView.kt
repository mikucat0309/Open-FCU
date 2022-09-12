package at.mikuc.openfcu.timetable

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.theme.OpenFCUTheme

private val SECTION_HEIGHT = 90.dp
private val SECTION_WIDTH = 120.dp
private val HEADER_HEIGHT = 20.dp
private val HEADER_WIDTH = 90.dp
private fun Modifier.sectionBorder() = border(1.dp, color = Color.Gray)

private val sectionTimes = listOf(
    "",
    "8:10-9:00",
    "9:10-10:00",
    "10:10-11:00",
    "11:10-12:00",
    "12:10-13:00",
    "13:10-14:00",
    "14:10-15:00",
    "15:10-16:00",
    "16:10-17:00",
    "17:10-18:00",
    "18:30-19:20",
    "19:25-20:15",
    "20:25-21:15",
    "21:20-22:10",
)
private val days = listOf(
    "",
    "一",
    "二",
    "三",
    "四",
    "五",
    "六",
    "日",
)

@Composable
fun TimetableView(viewModel: TimetableViewModel) {
    LaunchedEffect(Unit) {
        viewModel.fetchTimetable()
    }
    Timetable(viewModel.state.sections)
}

@Composable
private fun Timetable(sections: List<Section>) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ) {
        Row(
            modifier = Modifier.sectionBorder()
        ) {
            SectionHeaderColumn()
            for (day in 1..7) {
                DayColumn(day, sections = sections.filter { it.day == day })
            }
        }
    }
}

@Composable
private fun SectionHeaderColumn() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(HEADER_WIDTH)
    ) {
        Spacer(
            Modifier
                .size(HEADER_WIDTH, HEADER_HEIGHT)
                .sectionBorder()
        )
        for (section in 1..14) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SECTION_HEIGHT)
                    .sectionBorder()
            ) {
                Text(section.toString())
                Text(sectionTimes[section])
            }
        }
    }
}

@Composable
private fun DayColumn(day: Int, sections: List<Section>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(SECTION_WIDTH, HEADER_HEIGHT)
                .sectionBorder()
        ) {
            Text(text = days[day])
        }
        for (section in 1..14) {
            Section(section = sections.firstOrNull { it.section == section })
        }
    }
}

@Composable
private fun Section(section: Section?) {
    val modifier = Modifier
        .size(width = SECTION_WIDTH, height = SECTION_HEIGHT)
        .sectionBorder()
    if (section != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(2.dp)
        ) {
            Text(
                text = section.name,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = section.location,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )
        }
    } else {
        Spacer(modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun TimetablePreview() {
    val tt = listOf(
        Section(
            method = 4,
            memo = "Memo",
            time = "11:00-12:00",
            location = "資電館 234 電腦教室電腦教室電腦教室",
            section = 1,
            day = 1,
            name = "微處理機介面設計設計設計設計"
        ),
        Section(
            method = 4,
            memo = "Memo",
            time = "11:00-12:00",
            location = "home",
            section = 6,
            day = 2,
            name = "喵喵喵喵"
        )
    )
    OpenFCUTheme {
        Timetable(tt)
    }
}
