package com.example.projectapp.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

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