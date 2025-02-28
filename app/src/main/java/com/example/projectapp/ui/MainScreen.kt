
package com.example.projectapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projectapp.navigation.Screen
import com.example.projectapp.playButtonSound
import com.example.projectapp.ui.screens.HomeScreen
import com.example.projectapp.ui.screens.IncomeScreen
import com.example.projectapp.ui.screens.InputScreen
import com.example.projectapp.ui.screens.OutcomeScreen

@Composable
fun MainScreenWithNavHost(
    isDarkTheme: Boolean,
    isEnglish: Boolean,
    onThemeToggle: () -> Unit,
    onLanguageToggle: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                isEnglish = isEnglish,
                onItemClicked = { playButtonSound(context) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            composable("home") {
                HomeScreen(
                    isEnglish = isEnglish,
                    onNavigateToIncome = {
                        playButtonSound(context)
                        navController.navigate("income")
                    },
                    onThemeToggle = onThemeToggle,
                    onLanguageToggle = onLanguageToggle
                )
            }
            composable("income") {
                IncomeScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        navController.navigateUp()
                    }
                )
            }
            composable("input") {
                InputScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        navController.navigateUp()
                    },
                    onConfirm = { _ ->
                        playButtonSound(context)
                        navController.navigateUp()
                    }
                )
            }
            composable("outcome") {
                OutcomeScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    isEnglish: Boolean,
    onItemClicked: () -> Unit
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "income",
            onClick = {
                if (currentRoute != "income") {
                    onItemClicked()
                    navController.navigate("income") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(if (isEnglish) "Income" else "รายรับ") },
            icon = {}
        )
        NavigationBarItem(
            selected = currentRoute == "input",
            onClick = {
                if (currentRoute != "input") {
                    onItemClicked()
                    navController.navigate("input") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(if (isEnglish) "Input" else "ป้อนข้อมูล") },
            icon = {}
        )
        NavigationBarItem(
            selected = currentRoute == "outcome",
            onClick = {
                if (currentRoute != "outcome") {
                    onItemClicked()
                    navController.navigate("outcome") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            label = { Text(if (isEnglish) "Outcome" else "รายจ่าย") },
            icon = {}
        )
    }
}