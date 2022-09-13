package at.mikuc.openfcu.course.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import at.mikuc.openfcu.theme.OpenFCUTheme
import at.mikuc.openfcu.util.day2str

private val yearOptions = (105..111).associateWith { it.toString() }
private val semesterOptions = mapOf(
    1 to "上學期",
    2 to "下學期",
    3 to "暑修上",
    4 to "暑修下",
)
private val creditOptions = (0..9).associateWith { it.toString() }

@Composable
fun CourseSearchView(viewModel: CourseSearchViewModel = hiltViewModel()) {
    val state = viewModel.state
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Yellow.copy(alpha = 0.4f))
                .padding(4.dp)
        ) {
            Text(
                text = "此功能資料來源為學校課程檢索 API，故科目名稱、教師名稱、選課代碼、星期、節次必須至少選擇一項",
                style = MaterialTheme.typography.body1
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(modifier = Modifier.padding(top = 8.dp)) { YearInputField(
                    year = state.year,
                    onUpdate = { viewModel.updateYear(it) },
                    modifier = Modifier.weight(1f)
                )
                SemesterInputField(
                    semester = state.semester,
                    onUpdate = { viewModel.updateSemester(it) },
                    modifier = Modifier.weight(1f)
                )
            }
            Divider(Modifier.fillMaxWidth().padding(top = 16.dp))
            Row(modifier = Modifier.padding(top = 8.dp)) {
                CourseNameInputField(
                    courseName = state.name,
                    onUpdate = { viewModel.updateCourseName(it) },
                    modifier = Modifier.weight(1f)
                )
                TeacherNameInputField(
                    teacher = state.teacher,
                    onUpdate = { viewModel.updateTeacher(it) },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                CodeInputField(
                    code = state.code,
                    onUpdate = { viewModel.updateCode(it) },
                    modifier = Modifier.weight(2f)
                )
                CreditInputField(
                    credit = state.credit,
                    onUpdate = { viewModel.updateCredit(it) },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                OpenerNameInputField(
                    openerName = state.openerName,
                    onUpdate = { viewModel.updateOpenerName(it) },
                    modifier = Modifier.weight(2f)
                )
                AcceptStudentInputField(
                    openNum = state.openNum,
                    onUpdate = { viewModel.updateOpenNum(it) },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                LocationInputField(
                    location = state.location,
                    onUpdate = { viewModel.updateLocation(it) },
                    modifier = Modifier.weight(2f)
                )
                DayOfWeekInputField(
                    day = state.day,
                    onUpdate = { viewModel.updateDay(it) },
                    modifier = Modifier.weight(1f)
                )
            }
            SectionInputField(
                sections = state.sections,
                onUpdate = { viewModel.updateSections(it) }
            )
        }
    }
}

@Composable
private fun SectionInputField(
    sections: Set<Int>,
    onUpdate: (Set<Int>) -> Unit
) {
    Column(modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp)) {
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
                for (index in 1..7) {
                    SectionButton(
                        index = index,
                        value = index in sections,
                        onUpdate = {
                            onUpdate(if (index in sections) sections - it else sections + it)
                        },
                        Modifier.weight(1f)
                    )
                }
            }
            Row {
                for (index in 8..14) {
                    SectionButton(
                        index = index,
                        value = index in sections,
                        onUpdate = {
                            onUpdate(if (it in sections) sections - it else sections + it)
                        },
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayOfWeekInputField(
    day: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier
) {
    MyOptionalDropdownMenu(
        "星期",
        map = day2str,
        value = day,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun LocationInputField(
    location: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier
) {
    MyTextInputField(
        label = "上課地點",
        value = location,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun AcceptStudentInputField(
    openNum: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier
) {
    MyNumberInputField(
        label = "開放修課人數",
        value = openNum,
        onUpdate = { if (it == null || it in 0..999) onUpdate(it) },
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun OpenerNameInputField(
    openerName: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier
) {
    MyTextInputField(
        label = "開課單位名稱",
        value = openerName,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun CreditInputField(
    credit: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier
) {
    MyOptionalDropdownMenu(
        label = "學分數",
        map = creditOptions,
        value = credit,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun CodeInputField(
    code: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier
) {
    MyNumberInputField(
        "選課代碼",
        value = code,
        onUpdate = { if (it == null || it in 1..9999) onUpdate(it) },
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun TeacherNameInputField(
    teacher: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier
) {
    MyTextInputField(
        label = "教師名稱",
        value = teacher,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun CourseNameInputField(
    courseName: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier
) {
    MyTextInputField(
        label = "科目名稱",
        value = courseName,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun SemesterInputField(
    semester: Int,
    onUpdate: (Int) -> Unit,
    modifier: Modifier
) {
    MyDropdownMenu(
        label = "學期",
        map = semesterOptions,
        value = semester,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun YearInputField(
    year: Int,
    onUpdate: (Int) -> Unit,
    modifier: Modifier
) {
    MyDropdownMenu(
        label = "學年度",
        map = yearOptions,
        value = year,
        onUpdate = onUpdate,
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun MyTextInputField(
    label: String,
    value: String?,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier
    ) {
        Text(label)
        OutlinedTextField(
            value = value ?: "",
            onValueChange = { onUpdate(it) },
            singleLine = true,
            keyboardOptions = keyboardOptions,
        )
    }
}

@Composable
private fun MyNumberInputField(
    label: String,
    value: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    MyTextInputField(
        label = label,
        value = value?.toString(10) ?: "",
        onUpdate = { if (it.isDigitsOnly()) onUpdate(it.toIntOrNull()) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyOptionalDropdownMenu(
    label: String,
    map: Map<Int, String>,
    value: Int?,
    onUpdate: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
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
                        onUpdate(null)
                        expanded = false
                    }
                ) {
                    Text(text = "(不指定)")
                }
                map.entries.forEach { entry ->
                    DropdownMenuItem(
                        onClick = {
                            onUpdate(entry.key)
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
    onUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = map[value]!!,
                onValueChange = {},
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
                            onUpdate(entry.key)
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
    OpenFCUTheme {
        CourseSearchView()
    }
}

@Composable
fun SectionButton(
    index: Int,
    value: Boolean,
    onUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = MaterialTheme.colors
    val textColor = if (value) color.primary else color.onSurface.copy(alpha = 0.6f)
    val buttonColor = if (value) color.primary.copy(alpha = 0.2f) else color.surface
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(buttonColor)
            .clickable(onClick = { onUpdate(index) })
            .padding(4.dp)
    ) {
        Text(
            text = index.toString(),
            style = MaterialTheme.typography.body1.copy(fontSize = 18.sp, color = textColor),
            textAlign = TextAlign.Center,
        )
    }
}
