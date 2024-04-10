package com.anudeep.hydropal

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

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        IntakeList.initializeDatabase(application)
        super.onCreate(savedInstanceState)
        setContent {
            HydroPalTheme {
                val items=listOf(
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
                    var navController= rememberNavController()
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
                    Scaffold (
                        modifier=Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemindex == index,
                                        label = { Text(item.title)},
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
                                NavHost(navController=navController,startDestination = "Home"){
                                    composable("Home"){
                                        Home()
                                    }
                                    composable("History"){
                                        History()
                                    }
                                    composable("Settings"){
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