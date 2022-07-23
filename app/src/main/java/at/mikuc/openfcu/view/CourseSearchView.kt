package at.mikuc.openfcu.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import at.mikuc.openfcu.repository.FcuCourseSearchRepository
import at.mikuc.openfcu.ui.theme.OpenFCUTheme
import at.mikuc.openfcu.viewmodel.CourseSearchViewModel

@Composable
fun CourseSearchView(viewModel: CourseSearchViewModel) {
    val state = viewModel.state
    val yearOptions = (105..111).associateWith { it.toString() }
    val semesterOptions = mapOf(
        1 to "上學期",
        2 to "下學期",
        3 to "暑修上",
        4 to "暑修下",
    )
    val creditOptions = mapOf(
        0 to "0",
        1 to "1",
        2 to "2",
        3 to "3",
        4 to "4",
        5 to "5",
        6 to "6",
        7 to "7",
        8 to "8",
    )
    val days = mapOf(
        1 to "一",
        2 to "二",
        3 to "三",
        4 to "四",
        5 to "五",
        6 to "六",
        7 to "日",
    )
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row {
            MyDropdownMenu(
                label = "學年度",
                map = yearOptions,
                value = state.year,
                update = { viewModel.state = state.copy(year = it) },
                modifier = Modifier.weight(1f)
            )

            MyDropdownMenu(
                label = "學期",
                map = semesterOptions,
                value = state.semester,
                update = { viewModel.state = state.copy(semester = it) },
                modifier = Modifier.weight(1f)
            )
        }

        Divider(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Row {
            MyTextField(
                label = "科目名稱",
                value = state.name,
                update = { viewModel.state = state.copy(name = it) },
                modifier = Modifier.weight(1f)
            )
            MyTextField(
                label = "教師名稱",
                value = state.teacher,
                update = { viewModel.state = state.copy(teacher = it) },
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            MyNumberField(
                "選課代碼",
                value = state.code,
                update = {
                    if (it == null || it in 1..9999) viewModel.state = state.copy(code = it)
                },
                modifier = Modifier.weight(2f)
            )
            MyOptionalDropdownMenu(
                label = "學分數",
                map = creditOptions,
                value = state.credit,
                update = { viewModel.state = state.copy(credit = it) },
                Modifier.weight(1f)
            )
        }
        Row {
            MyTextField(
                label = "開課單位名稱",
                value = state.openerName,
                update = { viewModel.state = state.copy(openerName = it) },
                modifier = Modifier.weight(2f)
            )
            MyNumberField(
                label = "開放修課人數",
                value = state.openNum,
                update = {
                    if (it == null || it in 0..999) viewModel.state = state.copy(openNum = it)
                },
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            MyTextField(
                "上課地點",
                state.location,
                update = { viewModel.state = state.copy(location = it) },
                Modifier.weight(2f)
            )
            MyOptionalDropdownMenu(
                "星期",
                map = days,
                value = state.day,
                update = { viewModel.state = state.copy(day = it) },
                modifier = Modifier.weight(1f),
            )
        }
        Text("節數")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)
        ) {
            Row {
                (1..7).forEach { index ->
                    SectionButton(
                        index = index,
                        value = state.sections,
                        update = { viewModel.state = state.copy(sections = it) },
                        Modifier.weight(1f)
                    )
                }
            }
            Row {
                (8..14).forEach { index ->
                    SectionButton(
                        index = index,
                        value = state.sections,
                        update = { viewModel.state = state.copy(sections = it) },
                        Modifier.weight(1f)
                    )
                }
            }
        }

    }

}

@Composable
private fun MyTextField(
    label: String,
    value: String?,
    update: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(label)
        OutlinedTextField(
            value = value ?: "",
            onValueChange = { update(it) },
            singleLine = true,
        )
    }
}

@Composable
private fun MyNumberField(
    label: String,
    value: Int?,
    update: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    MyTextField(
        label = label,
        value = value?.toString(10) ?: "",
        update = { if (it.isDigitsOnly()) update(it.toIntOrNull()) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyOptionalDropdownMenu(
    label: String,
    map: Map<Int, String>,
    value: Int?,
    update: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value?.let { map[it] } ?: "",
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        update(null)
                        expanded = false
                    }
                ) {
                    Text(text = "(不指定)")
                }
                map.entries.forEach { entry ->
                    DropdownMenuItem(
                        onClick = {
                            update(entry.key)
                            expanded = false
                        }
                    ) {
                        Text(text = entry.value)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyDropdownMenu(
    label: String,
    map: Map<Int, String>,
    value: Int,
    update: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Text(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = map[value]!!,
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                map.entries.forEach { entry ->
                    DropdownMenuItem(
                        onClick = {
                            update(entry.key)
                            expanded = false
                        }
                    ) {
                        Text(text = entry.value)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseSearchPreview() {
    val csvm = CourseSearchViewModel(
        FcuCourseSearchRepository()
    )
    OpenFCUTheme {
        CourseSearchView(csvm)
    }
}

@Composable
fun SectionButton(
    index: Int,
    value: Set<Int>,
    update: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colors
    val textColor = if (index in value) color.primary else color.onSurface.copy(alpha = 0.6f)
    val buttonColor =
        if (index in value) color.primary.copy(alpha = 0.2f) else color.surface
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(buttonColor)
            .padding(4.dp)
            .clickable(onClick = {
                update(if (index in value) value.minusElement(index) else value.plusElement(index))
            })
    ) {
        Text(
            text = index.toString(),
            style = MaterialTheme.typography.body1.copy(fontSize = 18.sp, color = textColor),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CourseSearchFAB(ctrl: NavHostController, viewModel: CourseSearchViewModel) {
    FloatingActionButton(onClick = {
        viewModel.search()
        ctrl.navigate(CourseGraph.Result.route) {
            popUpTo(CourseGraph.Search.route)
        }
    }) {
        Icon(Icons.Outlined.Search, "Search")
    }
}