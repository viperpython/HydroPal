package com.anudeep.hydropal

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import com.anudeep.hydropal.IntakeList.saveWaterIntake
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

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
                    Thread{
                        run {

                            IntakeList.deleteWaterIntake()
                        }
                    }.start()
                }
                if (totalWaterIntake > 0)
                    totalWaterIntake.minus(250)
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
                waterIntakeList.add(WaterIntake(amount = 250, timestamp = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)))
                //TODO
                Thread {
                    run {
                    saveWaterIntake(
                        WaterIntake(
                            amount = 250,
                            timestamp = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)
                        )
                    )
                    }
                totalWaterIntake.plus(250)
                }.start()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = "Add 250ml",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// ...