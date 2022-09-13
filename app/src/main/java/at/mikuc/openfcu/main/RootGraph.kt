package at.mikuc.openfcu.main

import at.mikuc.openfcu.course.CourseGraph
import at.mikuc.openfcu.util.Route

sealed class RootGraph(override val route: Route) : Graph {
    object Redirect : RootGraph("redirect")
    object Setting : RootGraph("setting")
    object QrCode : RootGraph("qrcode")
    object Course : RootGraph("course")
    object Timetable : RootGraph("timetable")
    object Pass : RootGraph("pass")

    object Empty : RootGraph("EMPTY")

    companion object {
        fun fromRoute(route: Route?): Graph {
            if (route == null) return Empty
            return when (route.split('/').first()) {
                Redirect.route -> Redirect
                Setting.route -> Setting
                QrCode.route -> QrCode
                Course.route -> CourseGraph.fromRoute(route)
                Timetable.route -> Timetable
                Pass.route -> Pass
                else -> Empty
            }
        }
    }
}
