package at.mikuc.openfcu.home

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.mikuc.openfcu.redirect.LinkButton
import at.mikuc.openfcu.redirect.RedirectItem
import at.mikuc.openfcu.redirect.RedirectViewModel
import at.mikuc.openfcu.redirect.redirectCallback
import at.mikuc.openfcu.theme.MaterialTheme3
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.timetable.Section
import at.mikuc.openfcu.timetable.SectionList
import at.mikuc.openfcu.timetable.TimetableViewModel
import at.mikuc.openfcu.util.currentSection
import at.mikuc.openfcu.util.getActivity
import at.mikuc.openfcu.util.getActivityViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import java.time.LocalDateTime

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeView(
    redirectVM: RedirectViewModel = getActivityViewModel(),
    timetableVM: TimetableViewModel = getActivityViewModel(),
) {
    val onClick: (RedirectItem) -> Unit = { redirectVM.fetchRedirectToken(it.service) }
    val today = LocalDateTime.now()
    val currentSection = today.currentSection
    val todayOfWeek = today.dayOfWeek.value % 7
    PureHomeView(
        redirectVM.links,
        onClick,
        timetableVM.sections,
        todayOfWeek,
        currentSection,
        false,
        {},
        timetableVM.captcha
    )
    val activity = LocalContext.current.getActivity() ?: return
    val service = activity.intent.getStringExtra("redirect_service")
    if (service != null) {
        redirectVM.fetchRedirectToken(service)
    }
    redirectCallback(activity, redirectVM)
}

@Composable
private fun PureHomeView(
    items: List<RedirectItem>,
    onClick: (RedirectItem) -> Unit,
    sections: List<Section>,
    todayOfWeek: Int,
    currentSection: Int?,
    isClocked: Boolean,
    clock: (String) -> Unit,
    bitmap: Bitmap?
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme3.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryTitle("跳轉連結")
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items.take(4)
                    .forEach {
                        LinkButton(it.title) {
                            onClick(it)
                        }
                    }
            }
            CategoryTitle("今日課表")
            SectionList(
                sections = sections,
                currentDay = todayOfWeek,
                currentSection = currentSection,
                isClocked = isClocked,
                clock = clock,
                bitmap = bitmap
            )
        }
    }
}

@Composable
private fun CategoryTitle(category: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(category, style = MaterialTheme.typography.h6)
        Spacer(Modifier.width(4.dp))
        Text("view all", style = MaterialTheme.typography.caption)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 584)
@Composable
private fun HomePreview() {
    val links = listOf(
        RedirectItem("iLearn 2.0", ""),
        RedirectItem("MyFCU", ""),
        RedirectItem("空間借用", ""),
        RedirectItem("學生請假", ""),
        RedirectItem("行動刷卡", ""),
    )
    val sections = listOf(
        Section(
            method = 4,
            memo = "",
            time = "10:10-11:00",
            location = "語205",
            section = 3,
            day = 1,
            name = "纖維染色學1"
        ),
        Section(
            method = 4,
            memo = "",
            time = "11:10-12:00",
            location = "語205",
            section = 4,
            day = 1,
            name = "纖維染色學2"
        ),
        Section(
            method = 4,
            memo = "",
            time = "10:10-11:00",
            location = "語205",
            section = 3,
            day = 2,
            name = "纖維染色學3"
        ),
        Section(
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
        PureHomeView(links, {}, sections, 3, 3, true, {}, null)
    }
}
