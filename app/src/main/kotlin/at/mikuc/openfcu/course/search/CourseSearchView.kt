package at.mikuc.openfcu.course.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Business
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.course.search.options.CreditExtraOption
import at.mikuc.openfcu.course.search.options.DayExtraOption
import at.mikuc.openfcu.course.search.options.ExtraOptions
import at.mikuc.openfcu.course.search.options.LocationExtraOption
import at.mikuc.openfcu.course.search.options.OpenerNameExtraOption
import at.mikuc.openfcu.course.search.options.SectionsExtraOption
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.theme.MaterialTheme3
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
private val sectionsOptions = (1..14).associateWith { it.toString() }

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
    var extraField = remember { mutableStateListOf<ExtraOptions>() }


    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ExtraConditionDialog(
            setShowDialog = {
                showDialog = it
            }
        ) {
            extraField.add(it)
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(Modifier.fillMaxWidth()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Yellow.copy(alpha = 0.4f))
//                    .padding(4.dp)
//            ) {
//                Text(
//                    text = "此功能資料來源為學校課程檢索 API，故科目名稱、教師名稱、選課代碼、星期、節次必須至少選擇一項",
//                    style = MaterialTheme.typography.subtitle2
//                )
//            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text("基本條件", fontSize = 24.sp)
                }
                // 基本條件
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    MyTextInputField(
                        label = "課程名稱",
                        value = courseName,
                        onUpdate = { courseName = it },
                        modifier = Modifier
//                            .padding(horizontal = 4.dp)
//                            .width(328.dp)
                            .fillMaxWidth(),
                        textFieldModifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
//                    val modifier = Modifier
//                        .weight(3.0f)
//                        .padding(horizontal = 4.dp)
                    MyTextInputField(
                        label = "教師名稱",
                        value = teacherName,
                        onUpdate = { teacherName = it },
                        modifier = Modifier
//                            .padding(horizontal = 4.dp)
//                            .width(192.dp)
                            .weight(3.0f)
                    )
                    MyNumberInputField(
                        "選課代碼",
                        value = code,
                        onUpdate = { if (it == null || it in 1..9999) code = it },
                        modifier = Modifier
//                            .padding(horizontal = 4.dp)
//                            .width(120.dp)
                            .weight(2.0f)
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
//                    val modifier = Modifier
//                        .weight(1.0f)
//                        .padding(horizontal = 4.dp)
                    MyOptionalDropdownMenu(
                        "星期",
                        map = day2str,
                        value = day,
                        onUpdate = { day = it },
                        modifier = Modifier
//                            .width(192.dp)
                            .weight(3.0f)
                    )
                    MyOptionalDropdownMenu(
                        "節次",
                        map = sectionsOptions,
                        value = day,
                        onUpdate = { day = it },
                        modifier = Modifier
//                            .width(120.dp)
                            .weight(2.0f)
                    )
                }
                // 額外條件
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text("額外條件", fontSize = 24.sp)
                }
//                Row(modifier = Modifier.padding(top = 8.dp)) {
//                    val modifier = Modifier
////                        .weight(1.0f)
//                        .padding(horizontal = 4.dp)
//                    MyDropdownMenu(
//                        label = "學年度",
//                        map = yearOptions,
//                        value = year,
//                        onUpdate = { year = it },
//                        modifier = modifier
//                    )
//                    MyDropdownMenu(
//                        label = "學期",
//                        map = semesterOptions,
//                        value = semester,
//                        onUpdate = { semester = it },
//                        modifier = modifier.padding(horizontal = 4.dp)
//                    )
//                }

                extraField.forEachIndexed { index, item ->
                    when (item) {
                        is LocationExtraOption -> {
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                val modifier = Modifier
//                        .weight(1.0f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                MyTextInputField(
                                    label = "上課地點",
                                    value = item.text,
                                    onUpdate = {
                                        extraField[index] = item.copy(it)
                                    },
                                    modifier = modifier,
                                    textFieldModifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        is OpenerNameExtraOption -> {
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                val modifier = Modifier
//                        .weight(1.0f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                MyTextInputField(
                                    label = "開課單位",
                                    value = item.text,
                                    onUpdate = {
                                        extraField[index] = item.copy(it)
                                    },
                                    modifier = modifier,
                                    textFieldModifier = Modifier.fillMaxWidth()
                                )
//                    MyNumberInputField(
//                        label = "開放修課人數",
//                        value = openNum,
//                        onUpdate = { if (it == null || it in 0..999) openNum = it },
//                        modifier = modifier
//                    )
                            }
                        }
                        is CreditExtraOption -> {
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                val modifier = Modifier
//                        .weight(1.0f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                MyOptionalDropdownMenu(
                                    label = "學分數",
                                    map = creditOptions,
                                    value = item.value,
                                    onUpdate = {
                                        extraField[index] = item.copy(it)
                                    },
                                    modifier = modifier,
                                    textFieldModifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        is DayExtraOption -> {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val modifier = Modifier
                                    .weight(1.0f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                MyOptionalDropdownMenu(
                                    "星期",
                                    map = day2str,
                                    value = item.value,
                                    onUpdate = {
                                        extraField[index] = item.copy(it)
                                    },
                                    modifier = modifier,
                                    textFieldModifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        is SectionsExtraOption -> {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val modifier = Modifier
                                    .weight(1.0f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth()
                                MyOptionalDropdownMenu(
                                    "節次",
                                    map = sectionsOptions,
                                    value = item.value,
                                    onUpdate = {
                                        extraField[index] = item.copy(it)
                                    },
                                    modifier = modifier,
                                    textFieldModifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                }

//                SectionInputField(
//                    sections = sections,
//                    onUpdate = { sections = it }
//                )
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("新增額外條件...")
                    }
                }
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
fun ExtraConditionDialog(
    setShowDialog: (Boolean) -> Unit,
    extraField: (ExtraOptions) -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(shape = RoundedCornerShape(8.dp)) {
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(260.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Row() {
                        Text("新增額外條件", fontSize = 20.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        Row(
                            modifier = Modifier
                                .width(248.dp)
                                .height(40.dp)
                        ) {
                            Button(
                                onClick = {
                                    setShowDialog(false)
                                    extraField(LocationExtraOption(""))
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(Icons.Rounded.LocationOn, contentDescription = "")
                                    Text("上課地點")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .width(248.dp)
                                .height(40.dp)
                        ) {
                            Button(
                                onClick = {
                                    setShowDialog(false)
                                    extraField(OpenerNameExtraOption(""))
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(Icons.Rounded.Business, contentDescription = "")
                                    Text("開課單位")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .width(248.dp)
                                .height(40.dp)
                        ) {
                            Button(
                                onClick = {
                                    setShowDialog(false)
                                    extraField(CreditExtraOption(null))
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(Icons.Rounded.Book, contentDescription = "")
                                    Text("學分數")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .width(248.dp)
                                .height(40.dp)
                        ) {
                            Button(
                                onClick = {
                                    setShowDialog(false)
                                    extraField(DayExtraOption(null))
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(Icons.Rounded.CalendarToday, contentDescription = "")
                                    Text("星期")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .width(248.dp)
                                .height(40.dp)
                        ) {
                            Button(
                                onClick = {
                                    setShowDialog(false)
                                    extraField(SectionsExtraOption(null))
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.surface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(Icons.Rounded.CalendarToday, contentDescription = "")
                                    Text("節次")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
fun ExtraConditionDialogPreview() {
    MixMaterialTheme {
        CompositionLocalProvider(
            LocalNavHostController provides rememberNavController()
        ) {
            ExtraConditionDialog(
                setShowDialog = {
                    1
                },
                extraField = {
                    listOf<ExtraOptions>()
                }
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
private fun MyTextInputField(
    label: String,
    value: String?,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier
    ) {
//        Text(label)
        TextField(
            label = { Text(label ?: "") },
            value = value ?: "",
            onValueChange = { onUpdate(it) },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme3.colorScheme.surface),
            modifier = textFieldModifier
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
    textFieldModifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
//        Text(label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                label = { Text(label ?: "") },
                value = value?.let { map[it] } ?: "",
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = textFieldModifier
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
            TextField(
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
