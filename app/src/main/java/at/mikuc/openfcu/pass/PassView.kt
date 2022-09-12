package at.mikuc.openfcu.pass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import at.mikuc.openfcu.main.RootGraph
import at.mikuc.openfcu.theme.OpenFCUTheme
import java.time.ZoneId
import java.time.ZonedDateTime

fun NavGraphBuilder.passView() {
    composable(RootGraph.Pass.route) {
        PassView()
    }
}

@Composable
fun PassView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Pass()
    }
}

@Composable
private fun Pass() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .widthIn(0.dp, 500.dp)
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(vertical = 6.dp)
    ) {
        Title()
        Divider(Modifier.padding(vertical = 8.dp))
        Circle()
        Divider(Modifier.padding(vertical = 6.dp))
        Footer()
    }
}

@Composable
private fun Title() {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(0.9f)
    ) {
        Text("PASS", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W700))
    }
}

@Composable
private fun Footer() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Button(
            onClick = {},
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF286090),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(54.dp, 34.dp)
        ) {
            Text("關閉", style = TextStyle(fontSize = 14.sp))
        }
    }
}

@Composable
private fun Circle(datetime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC+8"))) {
    val colors = arrayOf(
        Color(248, 202, 18),
        Color(0, 107, 86),
        Color(0, 167, 222),
        Color(0, 108, 146),
        Color(122, 78, 155),
        Color(213, 128, 178),
        Color(226, 116, 16),
    )
    val color = colors[datetime.dayOfWeek.value % 7]
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(12.dp)
            .size(240.dp)
            .clip(CircleShape)
            .background(color)
            .padding(8.dp, 24.dp, 8.dp, 8.dp)
    ) {
        val fcuStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.White
        )
        ProvideTextStyle(fcuStyle) {
            val month = datetime.monthValue
            val day = datetime.dayOfMonth
            Text("$month/$day", style = TextStyle(fontSize = 60.sp, color = Color.White))
            Text("初音喵", modifier = Modifier.padding(vertical = 2.dp))
            Text("自主健康管理", modifier = Modifier.padding(vertical = 2.dp))
            Text("PASS", modifier = Modifier.padding(vertical = 4.dp))
            Spacer(Modifier.padding(2.dp))
            Text("Mikucat 關心您", style = fcuStyle.copy(fontSize = 10.sp))
            Spacer(Modifier.padding(4.dp))
        }
    }
}

@Preview
@Composable
fun PassCircle1Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now())
    }
}

@Preview
@Composable
fun PassCircle2Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(1))
    }
}

@Preview
@Composable
fun PassCircle3Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(2))
    }
}

@Preview
@Composable
fun PassCircle4Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(3))
    }
}

@Preview
@Composable
fun PassCircle5Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(4))
    }
}

@Preview
@Composable
fun PassCircle6Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(5))
    }
}

@Preview
@Composable
fun PassCircle7Preview() {
    OpenFCUTheme {
        Circle(ZonedDateTime.now().plusDays(6))
    }
}

@Preview(showBackground = true)
@Composable
fun PassPreview() {
    OpenFCUTheme {
        PassView()
    }
}
