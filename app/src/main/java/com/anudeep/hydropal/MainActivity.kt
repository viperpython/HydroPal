package com.anudeep.hydropal

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anudeep.hydropal.ui.theme.HydroPalTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
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
        if (jobScheduler.allPendingJobs.isEmpty()) {
            val jobInfo = JobInfo.Builder(1, ComponentName(this, MyJobService::class.java))
                .setPeriodic(60 * 60 * 1000) // Every hour
                .build()
            jobScheduler.schedule(jobInfo)
            Log.d("newJob", "new job scheduled")
        }

        for (allPendingJob in jobScheduler.allPendingJobs) {
            Log.d(
                "getcurrjobs",
                "${allPendingJob.id} ${allPendingJob.service.className} ${allPendingJob.isPeriodic} this"
            )
        }
        NotificationManager(this)
        var outItemIndex:Int=0
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
                var selectedItemindex by rememberSaveable {
                    mutableIntStateOf(outItemIndex)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
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
                                            outItemIndex=index
                                            //only keep current composable in backstack
                                            navController.popBackStack()
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
                                        Home(context)
                                    }
                                    composable("History") {
                                        History()
                                    }
                                    composable("Settings") {
                                        SettingsPage(context)
                                    }
                                }
                            }
                        }
                    )
                    var pressed  =0
                    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            if (pressed == 0) {
                                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                                pressed++
                                Thread {
                                    Thread.sleep(2000)
                                    pressed = 0
                                }.start()
                            } else {
                                finish()
                            }
                        }
                    })
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
