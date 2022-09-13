package at.mikuc.openfcu.course

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import at.mikuc.openfcu.course.search.CourseSearchResultView
import at.mikuc.openfcu.course.search.CourseSearchView
import at.mikuc.openfcu.course.search.CourseSearchViewModel
import at.mikuc.openfcu.main.Graph
import at.mikuc.openfcu.main.RootGraph
import at.mikuc.openfcu.util.Route

sealed class CourseGraph(route: Route) : Graph {
    override val route: Route = "${RootGraph.Course.route}/$route"

    object Search : CourseGraph("search")
    object Result : CourseGraph("result")
    object Detail : CourseGraph("detail")

    companion object {
        fun fromRoute(route: Route): CourseGraph {
            return when (route) {
                Search.route -> Search
                Result.route -> Result
                Detail.route -> Detail
                else -> throw IllegalArgumentException("Unknown route `$route`")
            }
        }
    }
}

fun NavGraphBuilder.addCourseGraph(viewModel: CourseSearchViewModel) {
    navigation(
        startDestination = CourseGraph.Search.route,
        route = RootGraph.Course.route
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
