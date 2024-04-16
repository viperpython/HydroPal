package com.anudeep.hydropal

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anudeep.hydropal.IntakeList.saveWaterIntake
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddWaterIntakeButton(waterIntakeList: MutableList<WaterIntake>,totalWaterIntake: Int, context: Context){
    val sp = context.getSharedPreferences("HydroPal", Context.MODE_PRIVATE)
    val glassSize = sp.getInt("glassSize", 250)
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Button(
            onClick = {
                if (waterIntakeList.isNotEmpty()){
                    waterIntakeList.remove(waterIntakeList.last())
                    Thread{
                        run {

                            IntakeList.deleteWaterIntake()
                        }
                    }.start()
                }
                if (totalWaterIntake > 0)
                    totalWaterIntake.minus(glassSize)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = "Remove last intake",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Button(
            onClick = {
                waterIntakeList.add(WaterIntake(amount = glassSize, timestamp = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)))
                //TODO
                Thread {
                    run {
                    saveWaterIntake(
                        WaterIntake(
                            amount = glassSize,
                            timestamp = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)
                        )
                    )
                    }
                totalWaterIntake.plus(glassSize)
                }.start()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = "Add ${glassSize}ml",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// ...