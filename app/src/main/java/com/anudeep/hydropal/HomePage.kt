package com.anudeep.hydropal



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anudeep.hydropal.R.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

lateinit var remwaterIntakeList: SnapshotStateList<WaterIntake>
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(){
    WaterReminderApp()
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterReminderApp() {
    remwaterIntakeList = remember { mutableStateListOf<WaterIntake>() }
    val totalWaterIntake = remwaterIntakeList.sumOf { it.amount }
    val dailyGoal = 2000 // Daily water intake goal in milliliters
    val dataUsageAnimate = animateFloatAsState(
        targetValue = totalWaterIntake.toFloat(),
        animationSpec = tween(
            durationMillis = 1000
        ), label = ""
    )
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

    Greeting()
    Spacer(modifier = Modifier.height(40.dp))
//    WaterIntakeProgress(totalWaterIntake, dailyGoal)
    CircularProgressbar(
        name = "Water Intake",
        size = 200.dp,
        foregroundIndicatorColor = MaterialTheme.colorScheme.primary,
        shadowColor = Color.LightGray,
        indicatorThickness = 20.dp,
        dataUsage = (totalWaterIntake.toFloat() / dailyGoal.toFloat()) * 100,
        animationDuration = 1000,
        dataTextStyle = TextStyle(fontSize = 20.sp)
    )

    DisplayText(name = "", animateNumber = dataUsageAnimate , dataTextStyle = TextStyle(fontSize = 32.sp),usePercentage = false,targetValue = dailyGoal.toFloat())
    Spacer(modifier = Modifier.height(40.dp))
    AddWaterIntakeButton(remwaterIntakeList,totalWaterIntake)
    Spacer(modifier = Modifier.height(40.dp))
//    WaterIntakeList(waterIntakeList)
    }

}

@Composable
fun Greeting() {
    Text(
        text="Hi Anudeep!",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun WaterIntakeProgress(totalWaterIntake: Int, dailyGoal: Int) {
    val progress = (totalWaterIntake.toFloat() / dailyGoal.toFloat())
    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier
            .height(300.dp)
            .width(300.dp),

        trackColor = Color(color.blue),
        color = Color(color.blue),
        strokeWidth = 40.dp,
        strokeCap = StrokeCap.Round
    )
    Text(
        text = "${totalWaterIntake}ml / ${dailyGoal}ml",
        modifier = Modifier.padding(top = 8.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddWaterIntakeButton(waterIntakeList: MutableList<WaterIntake>,totalWaterIntake: Int) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {
                if (waterIntakeList.isNotEmpty()){
                waterIntakeList.remove(waterIntakeList.last())
                IntakeList.waterIntakeList.remove(IntakeList.waterIntakeList.last())
                }
                if (totalWaterIntake > 0)
                totalWaterIntake.minus(250)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Remove last intake",
                color = Color.White
            )
        }
    Button(
        onClick = {
            waterIntakeList.add(WaterIntake(amount = 250, timestamp = LocalDateTime.now()))
            IntakeList.waterIntakeList.add(WaterIntake(amount = 250, timestamp = LocalDateTime.now()))
            totalWaterIntake.plus(250)
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Add 250ml",
            color = Color.White
        )
    }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaterIntakeList(waterIntakeList: List<WaterIntake>) {
    LazyColumn {
        items(waterIntakeList) { intake ->
            WaterIntakeListItem(intake)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaterIntakeListItem(waterIntake: WaterIntake) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.outlineVariant),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (modifier = Modifier.padding(30.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            ){
            Text(text = "${waterIntake.amount}ml",color=MaterialTheme.colorScheme.onSurface,fontSize = 20.sp)
            Text(
                text = waterIntake.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

    }
}

@Composable
fun CircularProgressbar(
    name: String = "",
    size: Dp = 110.dp,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.secondary,
    shadowColor: Color = MaterialTheme.colorScheme.onSurface,
    indicatorThickness: Dp = 8.dp,
    dataUsage: Float = 60f,
    animationDuration: Int = 1000,
    dataTextStyle: TextStyle = TextStyle(fontSize = 12.sp),
) {
    // State to hold the data usage value for animation
    var dataUsageRemember by remember {
        mutableFloatStateOf(-1f)
    }

    // State for animating the data usage value
    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(
            durationMillis = animationDuration
        ), label = ""
    )

    // Trigger the LaunchedEffect to start the animation when the composable is first launched.
    LaunchedEffect(Unit) {
//        dataUsageRemember = dataUsage
        IntakeList.waterIntakeList.forEach {
            remwaterIntakeList.add(it)
        }

    }
    //Trigger when dataUsage value changes
    LaunchedEffect(dataUsage) {
        dataUsageRemember = dataUsage
    }

    // Box to hold the entire composable
    Box(
        modifier = Modifier
            .size(size)
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Canvas drawing for the circular progress bar
        Canvas(
            modifier = Modifier.size(size)
        ) {
            // Draw the shadow around the circular progress bar
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(shadowColor, Color.White),
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2),
                    radius = this.size.height / 2
                ),
                radius = this.size.height / 2,
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            // Draw the white background of the circular progress bar
            drawCircle(
                color = Color.White,
                radius = (size / 2 - indicatorThickness).toPx(),
                center = Offset(x = this.size.width / 2, y = this.size.height / 2)
            )

            // Calculate and draw the progress indicator
            val sweepAngle = (dataUsageAnimate.value) * 360 / 100
            drawArc(
                color = foregroundIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                size = Size(
                    width = (size - indicatorThickness).toPx(),
                    height = (size - indicatorThickness).toPx()
                ),
                topLeft = Offset(
                    x = (indicatorThickness / 2).toPx(),
                    y = (indicatorThickness / 2).toPx()
                )
            )
        }

        // Display text below the circular progress bar
        DisplayText(
            name = name,
            animateNumber = dataUsageAnimate,
            dataTextStyle = dataTextStyle,
            usePercentage = true
        )
    }

    // Spacer to add some padding below the circular progress bar
    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
private fun DisplayText(
    name: String,
    animateNumber: State<Float>,
    dataTextStyle: TextStyle,
    usePercentage: Boolean,
    targetValue: Float = 0f
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        // Display the name with bold font and ellipsis for overflow
        if (name!="")
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            style = dataTextStyle,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.primary
        )

        // Display the data usage percentage text
        if (usePercentage) {
            Text(
                text = (animateNumber.value).toInt().toString() + "%",
                style = dataTextStyle
            )
        } else {
            Text(
                text = animateNumber.value.toInt().toString()+"ml / "+targetValue.toInt().toString()+"ml",
                style = dataTextStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
data class WaterIntake(val amount: Int, val timestamp: LocalDateTime)