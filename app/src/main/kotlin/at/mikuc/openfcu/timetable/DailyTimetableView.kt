package at.mikuc.openfcu.timetable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.isDigitsOnly
import at.mikuc.openfcu.theme.M3
import at.mikuc.openfcu.theme.MaterialTheme3
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.currentSection
import at.mikuc.openfcu.util.day2str
import at.mikuc.openfcu.util.getActivityViewModel
import at.mikuc.openfcu.util.section2str
import com.ramcosta.composedestinations.annotation.Destination
import java.time.LocalDateTime

@Destination
@Composable
fun DailyTimetableView(viewModel: TimetableViewModel = getActivityViewModel()) {
    val today = LocalDateTime.now()
    val currentSection = today.currentSection
    val todayOfWeek = today.dayOfWeek.value % 7
    val dayOfMonthList = (0..6).map { today.plusDays((it - todayOfWeek).toLong()).dayOfMonth }
    PureDailyTimetableView(
        viewModel.sections,
        todayOfWeek,
        currentSection,
        dayOfMonthList,
        false,
        { viewModel.clock() },
        viewModel.captcha
    )
}

@Composable
private fun PureDailyTimetableView(
    sections: List<Section>,
    todayOfWeek: Int,
    currentSection: Int?,
    dayOfMonthList: List<Int>,
    isClocked: Boolean,
    clock: (String) -> Unit,
    bitmap: Bitmap?,
) {
    require(dayOfMonthList.size == 7)
    require(todayOfWeek in 0..6)
    require(currentSection == null || currentSection in 1..14)

    val currentDay = remember { mutableStateOf(todayOfWeek) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.height(64.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            for (i in 0..6) {
                DayButton(i, dayOfMonthList[i], currentDay)
            }
        }
        Divider()
        SectionList(
            sections,
            currentDay.value,
            currentSection,
            isClocked,
            clock,
            bitmap,
            Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SectionList(
    sections: List<Section>,
    currentDay: Int,
    currentSection: Int?,
    isClocked: Boolean,
    clock: (String) -> Unit,
    bitmap: Bitmap?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        sections
            .filter { it.day == currentDay }
            .sortedBy { it.section }
            .forEach {
                if (it.section == currentSection)
                    CurrentSectionCard(it.name, it.location, it.section, isClocked, clock, bitmap)
                else
                    SectionCard(it.name, it.location, it.section)
            }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
private fun TimetablePreview() {
    val sections = listOf(
        Section(
            method = 4,
            memo = "",
            time = "10:10-11:00",
            location = "語205",
            section = 3,
            day = 1,
            name = "纖維染色學1"
        ), Section(
            method = 4,
            memo = "",
            time = "11:10-12:00",
            location = "語205",
            section = 4,
            day = 1,
            name = "纖維染色學2"
        ), Section(
            method = 4,
            memo = "",
            time = "10:10-11:00",
            location = "語205",
            section = 3,
            day = 2,
            name = "纖維染色學3"
        ), Section(
            method = 4,
            memo = "",
            time = "11:10-12:00",
            location = "語205",
            section = 4,
            day = 3,
            name = "纖維染色學4"
        )
    )
    MixMaterialTheme {
        PureDailyTimetableView(
            sections,
            1,
            3,
            listOf(8, 9, 10, 11, 12, 13, 14),
            false,
            {},
            null
        )
    }
}

@Composable
private fun DayButton(dayOfWeek: Int, dayOfMonth: Int, currentDay: MutableState<Int>) {
    val isSelected = dayOfWeek == currentDay.value
    val color = MaterialTheme3.colorScheme.run { if (isSelected) primary else onSurface }
    Column(
        Modifier
            .fillMaxHeight()
            .width(48.dp)
            .clickable { currentDay.value = dayOfWeek },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Bottom)
    ) {
        Text(
            day2str[dayOfWeek]!!, color = color, style = MaterialTheme.typography.subtitle1
        )
        Text(
            dayOfMonth.toString(), color = color, style = MaterialTheme.typography.h6
        )
        val modifier = Modifier
            .size(30.dp, 3.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
        Box(
            if (isSelected) modifier.background(color) else modifier
        ) {}
    }
}

@Composable
fun SectionCard(
    name: String,
    location: String,
    section: Int,
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme3.colorScheme.surface)
            .background(M3.Light.surface3)
    ) {
        val centerLine = createGuidelineFromTop(0.5f)
        val (nameRef, locationRef, timeRef) = createRefs()
        Text(
            name, Modifier.constrainAs(nameRef) {
                bottom.linkTo(centerLine, 2.dp)
                start.linkTo(parent.start, 16.dp)
            }, style = MaterialTheme.typography.subtitle1
        )
        Text(
            location, Modifier.constrainAs(locationRef) {
                top.linkTo(centerLine, 2.dp)
                start.linkTo(parent.start, 16.dp)
            }, style = MaterialTheme.typography.subtitle2
        )
        Text(
            section2str[section], Modifier.constrainAs(timeRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 16.dp)
            }, style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun CurrentSectionCard(
    name: String,
    location: String,
    section: Int,
    isClocked: Boolean,
    clock: (String) -> Unit,
    bitmap: Bitmap?
) {
    val isOpenDialog = remember { mutableStateOf(false) }
    val buttonColor = if (isClocked) M3.Ref.tertiary90 else M3.Ref.secondary90
    val buttonText = if (isClocked) "已簽到" else "簽到"
    Column(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(buttonColor)
    ) {
        SectionCard(name, location, section)
        Box(
            Modifier
                .fillMaxWidth()
                .height(32.dp)
                .clickable { if (!isClocked) isOpenDialog.value = true },
            contentAlignment = Alignment.Center
        ) {
            Text(buttonText, style = MaterialTheme.typography.subtitle1)
        }
    }
    if (isOpenDialog.value) {
        ClockDialog({ isOpenDialog.value = false }, clock, bitmap)
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrentSectionCardPreview() {
    MixMaterialTheme {
        CurrentSectionCard("纖維染色學", "語206", 3, false,{} , null)
    }
}

@Preview(showBackground = true)
@Composable
private fun CurrentSectionClockedCardPreview() {
    MixMaterialTheme {
        CurrentSectionCard("纖維染色學", "語206", 4, true, {}, null)
    }
}

@Composable
private fun ClockDialog(
    onDismiss: () -> Unit,
    clock: (String) -> Unit,
    bitmap: Bitmap?,
) {
    var captchaAnswer by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme3.colorScheme.surface)
                .padding(24.dp, 16.dp, 8.dp, 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("手動簽到", style = MaterialTheme.typography.h6)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (bitmap != null) Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captcha",
                    Modifier
                        .requiredSize(80.dp, 40.dp)
                )
                else Box(
                    Modifier
                        .requiredSize(80.dp, 40.dp)
                        .background(Color.LightGray)
                ) {}
                TextField(
                    value = captchaAnswer,
                    onValueChange = {
                        isError = it.length != 4 || !it.isDigitsOnly()
                        captchaAnswer = it
                    },
                    Modifier.requiredSize(100.dp, 41.dp),
                    singleLine = true,
                    isError = isError,
                )
            }
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
                TextButton(onClick = {
                    if (!isError) {
                        clock(captchaAnswer)
                        onDismiss()
                    }
                }) {
                    Text("簽到")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClockDialogPreview() {
    MixMaterialTheme {
        Surface(
            Modifier.background(MaterialTheme3.colorScheme.surface)
        ) {
            ClockDialog({}, {}, null)
        }
    }
}
