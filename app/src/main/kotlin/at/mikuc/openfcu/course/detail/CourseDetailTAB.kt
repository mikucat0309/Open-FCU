package at.mikuc.openfcu.course.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import at.mikuc.openfcu.destinations.CourseAssessmentViewDestination
import at.mikuc.openfcu.destinations.CourseInfoViewDestination
import at.mikuc.openfcu.destinations.CoursePreCourseViewDestination
import at.mikuc.openfcu.destinations.CourseSearchResultViewDestination
import at.mikuc.openfcu.main.LocalScaffoldState
import at.mikuc.openfcu.theme.MixMaterialTheme
import at.mikuc.openfcu.util.LocalNavHostController
import at.mikuc.openfcu.util.currentOrThrow
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.launch

@Composable
fun CourseDetailTAB(title: String) {
    val controller = LocalNavHostController.currentOrThrow
    val expanded = remember { mutableStateOf(false) }
    val onClick = { page: Direction ->
        expanded.value = false
        controller.navigate(page) {
            popUpTo(CourseSearchResultViewDestination)
        }
    }
    PureCourseDetailTAB(expanded, onClick, title, controller)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PureCourseDetailTAB(
    expanded: MutableState<Boolean>,
    onClick: (Direction) -> Unit,
    title: String,
    controller: NavHostController
) {
    TopAppBar(
        title = {
            ExposedDropdownMenuBox(
                expanded.value,
                onExpandedChange = { expanded.value = !expanded.value }
            ){
                Row(
                    Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title)
                    Icon(Icons.Filled.ArrowDropDown, "detail menu")
                }
                DropdownMenu(
                    expanded.value,
                    onDismissRequest = { expanded.value = false },
                    Modifier.exposedDropdownSize()
                ) {
                    DropdownMenuItem(onClick = { onClick(CourseInfoViewDestination) }) {
                        Text("課程資訊")
                    }
                    DropdownMenuItem(onClick = { onClick(CoursePreCourseViewDestination) }) {
                        Text("前置課程")
                    }
                    DropdownMenuItem(onClick = { onClick(CourseAssessmentViewDestination) }) {
                        Text("評量方式")
                    }
//                DropdownMenuItem(onClick = { onClick(CourseInfoViewDestination) }) {
//                    Text("教材")
//                }
//                DropdownMenuItem(onClick = { onClick(CourseInfoViewDestination) }) {
//                    Text("授課進度")
//                }
                }
            }
        },
        navigationIcon = {
            val scope = rememberCoroutineScope()
            IconButton(onClick = { scope.launch { controller.popBackStack() } }) {
                Icon(Icons.Outlined.ArrowBack, "back")
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun CourseDetailTopBarPreview() {
    MixMaterialTheme {
        CompositionLocalProvider(
            LocalScaffoldState provides rememberScaffoldState(),
            LocalNavHostController provides rememberNavController()
        ) {
            CourseDetailTAB("Lorem ipsum")
        }
    }
}
