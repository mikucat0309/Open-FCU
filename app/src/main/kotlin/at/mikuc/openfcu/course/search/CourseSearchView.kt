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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import at.mikuc.openfcu.util.day2str
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import java.util.*

private val yearOptions = (105..111).associateWith { it.toString() }
private val semesterOptions = mapOf(
    1 to "上學期",
    2 to "下學期",
    3 to "暑修上",
    4 to "暑修下",
)
private val creditOptions = (0..9).associateWith { it.toString() }

@Destination
@Composable
fun CourseSearchView(viewModel: CourseSearchViewModel = getActivityViewModel()) {
    PureCourseSearchView(onSubmit = { viewModel.search(it) })
}

@Suppress("LongMethod")
@Composable
fun PureCourseSearchView(onSubmit: (SearchFilter) -> Unit) {

    var year by remember { mutableStateOf(111) }
    var semester by remember { mutableStateOf(1) }
    var courseName by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var code by remember { mutableStateOf<Int?>(null) }
    var credit by remember { mutableStateOf<Int?>(null) }
    var openerName by remember { mutableStateOf("") }
    var openNum by remember { mutableStateOf<Int?>(null) }
    var location by remember { mutableStateOf("") }
    var day by remember { mutableStateOf<Int?>(null) }
    var sections by remember { mutableStateOf(emptySet<Int>()) }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Yellow.copy(alpha = 0.4f))
                    .padding(4.dp)
            ) {
                Text(
                    text = "此功能資料來源為學校課程檢索 API，故科目名稱、教師名稱、選課代碼、星期、節次必須至少選擇一項",
                    style = MaterialTheme.typography.subtitle2
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 4.dp)
                    MyDropdownMenu(
                        label = "學年度",
                        map = yearOptions,
                        value = year,
                        onUpdate = { year = it },
                        modifier = modifier
                    )
                    MyDropdownMenu(
                        label = "學期",
                        map = semesterOptions,
                        value = semester,
                        onUpdate = { semester = it },
                        modifier = modifier.padding(horizontal = 4.dp)
                    )
                }
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 4.dp)
                    MyTextInputField(
                        label = "科目名稱",
                        value = courseName,
                        onUpdate = { courseName = it },
                        modifier = modifier
                    )
                    MyTextInputField(
                        label = "教師名稱",
                        value = teacherName,
                        onUpdate = { teacherName = it },
                        modifier = modifier
                    )
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 4.dp)
                    MyNumberInputField(
                        "選課代碼",
                        value = code,
                        onUpdate = { if (it == null || it in 1..9999) code = it },
                        modifier = modifier
                    )
                    MyOptionalDropdownMenu(
                        label = "學分數",
                        map = creditOptions,
                        value = credit,
                        onUpdate = { credit = it },
                        modifier = modifier
                    )
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 4.dp)
                    MyTextInputField(
                        label = "開課單位名稱",
                        value = openerName,
                        onUpdate = { openerName = it },
                        modifier = modifier
                    )
                    MyNumberInputField(
                        label = "開放修課人數",
                        value = openNum,
                        onUpdate = { if (it == null || it in 0..999) openNum = it },
                        modifier = modifier
                    )
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val modifier = Modifier
                        .weight(1.0f)
                        .padding(horizontal = 4.dp)
                    MyTextInputField(
                        label = "上課地點",
                        value = location,
                        onUpdate = { location = it },
                        modifier = modifier
                    )
                    MyOptionalDropdownMenu(
                        "星期",
                        map = day2str,
                        value = day,
                        onUpdate = { day = it },
                        modifier = modifier
                    )
                }
                SectionInputField(
                    sections = sections,
                    onUpdate = { sections = it }
                )
            }
        }
        val controller = LocalNavHostController.currentOrThrow
        CourseSearchFAB {
            val filter = SearchFilter(
                year = year,
                semester = semester,
                name = courseName,
                code = code,
                teacher = teacherName,
                day = day,
                sections = sections,
                location = location,
                credit = credit,
                openerName = openerName,
                openNum = openNum
            )
            if (filter.isValid())
                onSubmit(filter)
            controller.navigate(CourseSearchResultViewDestination)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
fun CourseSearchPreview() {
    MixMaterialTheme {
        CompositionLocalProvider(
            LocalNavHostController provides rememberNavController()
        ) {
            PureCourseSearchView(onSubmit = {})
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
