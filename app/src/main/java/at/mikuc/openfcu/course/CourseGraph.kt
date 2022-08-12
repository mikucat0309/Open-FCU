package at.mikuc.openfcu.course

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import at.mikuc.openfcu.Graph
import at.mikuc.openfcu.course.search.CourseSearchResultView
import at.mikuc.openfcu.course.search.CourseSearchView
import at.mikuc.openfcu.course.search.CourseSearchViewModel

sealed class CourseGraph(val route: String) {
    object Search : CourseGraph(Graph.Course.route + "/search")
    object Result : CourseGraph(Graph.Course.route + "/result")
    object Detail : CourseGraph(Graph.Course.route + "/detail")
}

fun NavGraphBuilder.courseGraph(viewModel: CourseSearchViewModel) {
    navigation(
        startDestination = CourseGraph.Search.route,
        route = Graph.Course.route
    ) {
        composable(CourseGraph.Search.route) {
            CourseSearchView(viewModel)
        }
        composable(CourseGraph.Result.route) {
            CourseSearchResultView(viewModel)
        }
        composable(CourseGraph.Detail.route) {
        }
    }
}
