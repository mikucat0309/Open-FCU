package at.mikuc.openfcu.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import at.mikuc.openfcu.course.CourseGraph
import at.mikuc.openfcu.course.search.CourseSearchFAB
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.util.currentGraph

@Composable
fun MyFAB(
    ctrl: NavHostController,
    csvm: CourseSearchViewModel
) {
    when (ctrl.currentGraph()) {
        CourseGraph.Search -> CourseSearchFAB(ctrl, csvm)
    }
}
