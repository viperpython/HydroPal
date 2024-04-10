package com.anudeep.hydropal

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anudeep.hydropal.ui.theme.HydroPalTheme
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
            IntakeList.initializeDatabase(application)
            val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
                .setPeriodic(60 * 60 * 1000) // Every hour
                .build()
            jobScheduler.schedule(jobInfo)
            NotificationManager(this)
            setContent {
                HydroPalTheme {
                    val items = listOf(
                        BottomNavigationItem(
                            title = "Home",
                            selectedIcon = Icons.Filled.Home,
                            unselectedIcon = Icons.Outlined.Home
                        ),
                        BottomNavigationItem(
                            title = "History",
                            selectedIcon = Icons.Filled.Refresh,
                            unselectedIcon = Icons.Outlined.Refresh
                        ),
                        BottomNavigationItem(
                            title = "Settings",
                            selectedIcon = Icons.Filled.Settings,
                            unselectedIcon = Icons.Outlined.Settings
                        )
                    )
                    // A surface container using the 'background' color from the theme
                    var selectedItemindex by rememberSaveable {
                        mutableStateOf(0)
                    }
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
//                    NavHost(navController=navController,startDestination = "Home"){
//                        composable("Home"){
//                            Home()
//                        }
//                        composable("History"){
//                            History()
//                        }
//                        composable("Settings"){
//                            SettingsPage()
//                        }
//                }
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                NavigationBar {
                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedItemindex == index,
                                            label = { Text(item.title) },
                                            alwaysShowLabel = false,
                                            icon = {
                                                Icon(
                                                    imageVector = if (selectedItemindex == index) item.selectedIcon else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            },
                                            onClick = {
                                                selectedItemindex = index
                                                navController.navigate(item.title)
                                            },
                                        )
                                    }

                                }
                            },
                            content = { padding ->
                                Column(
                                    modifier = Modifier.padding(padding)
                                ) {
                                    NavHost(
                                        navController = navController,
                                        startDestination = "Home"
                                    ) {
                                        composable("Home") {
                                            Home()
                                        }
                                        composable("History") {
                                            History()
                                        }
                                        composable("Settings") {
                                            SettingsPage()
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        HydroPalTheme {
            Greeting("Android")
        }
    }
