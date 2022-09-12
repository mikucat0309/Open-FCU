package at.mikuc.openfcu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MyTitle(graph: Graph) {
    when (graph) {
        RootGraph.Redirect -> Text("快速跳轉")
        RootGraph.QrCode -> Text("數位 IC 卡 (QRCode)")
        RootGraph.Course -> Text("課程搜尋")
        RootGraph.Timetable -> Text("課表")
        RootGraph.Pass -> Text("自主健康管理")
        RootGraph.Setting -> Text("設定")
    }
}
