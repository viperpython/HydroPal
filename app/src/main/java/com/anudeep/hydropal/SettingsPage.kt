package com.anudeep.hydropal

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsPage(context: Context) {
    val sp = context.getSharedPreferences("HydroPal", Context.MODE_PRIVATE)
    val name = sp.getString("name", "Human") ?: "Human"
    val glassSize = sp.getInt("glassSize", 250)
    val dailyGoal = sp.getInt("dailyGoal", 2000)
    var showNameDialog by remember { mutableStateOf(false) }
    var showGlassSizeDialog by remember { mutableStateOf(false) }
    var showDailyGoalDialog by remember { mutableStateOf(false) }
    var remName by remember { mutableStateOf(name) }
    var remGlassSize by remember { mutableStateOf("$glassSize") }
    var remDailyGoal by remember { mutableStateOf("$dailyGoal") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge
        )
        SettingItem(
            label = "Name",
            value = remName
        ) { showNameDialog = true }
        SettingItem(
            label = "Glass Size (ml)",
            value = remGlassSize
        ) { showGlassSizeDialog = true }
        SettingItem(
            label = "Daily Goal (ml)",
            value = remDailyGoal
        ) { showDailyGoalDialog = true }
    }

    if (showNameDialog) {
        SettingDialog(
            title = "Name",
            value = remName,
            onValueChange = { remName = it ; sp.edit().putString("name", it).apply()},
            onDismissRequest = { showNameDialog = false }
        )
    }

    if (showGlassSizeDialog) {
        SettingDialog(
            title = "Glass Size (ml)",
            value = remGlassSize,
            onValueChange = { remGlassSize = it ; sp.edit().putInt("glassSize", it.toInt()).apply()},
            onDismissRequest = { showGlassSizeDialog = false }
        )
    }

    if (showDailyGoalDialog) {
        SettingDialog(
            title = "Daily Goal (ml)",
            value = remDailyGoal,
            onValueChange = { remDailyGoal = it ; sp.edit().putInt("dailyGoal", it.toInt()).apply()},
            onDismissRequest = { showDailyGoalDialog = false }
        )
    }
}

@Composable
fun SettingItem(
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = value,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun SettingDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var newValue by remember { mutableStateOf(value) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title,color = MaterialTheme.colorScheme.onSurface) },

        text = {
            OutlinedTextField(
                value = newValue,
                onValueChange = { newValue = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary
                )

            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onValueChange(newValue)
                    onDismissRequest()
                },

            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text(text = "Cancel")
            }
        },
        contentColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.background
    )
}