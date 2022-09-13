package at.mikuc.openfcu.main

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import at.mikuc.openfcu.course.CourseGraph

@Composable
fun MyTitle(graph: Graph) {
    when (graph) {
        RootGraph.Redirect -> Text("快速跳轉")
        RootGraph.QrCode -> Text("數位 IC 卡 (QRCode)")
        RootGraph.Timetable -> Text("課表")
        RootGraph.Pass -> Text("自主健康管理")
        RootGraph.Setting -> Text("設定")

        CourseGraph.Search -> Text("課程查詢")
        CourseGraph.Result -> Text("查詢結果")
    }
}
