package com.example.projectapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.projectapp.navigation.Screen
import com.example.projectapp.playButtonSound
import com.example.projectapp.ui.screens.HomeScreen
import com.example.projectapp.ui.screens.IncomeScreen
import com.example.projectapp.ui.screens.InputScreen
import com.example.projectapp.ui.screens.OutcomeScreen

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    isEnglish: Boolean,
    onThemeToggle: () -> Unit,
    onLanguageToggle: () -> Unit
) {
    var selectedScreen by remember { mutableStateOf(Screen.Home) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedScreen, isEnglish) { screen ->
                playButtonSound(context)
                selectedScreen = screen
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                Screen.Home -> HomeScreen(
                    isEnglish = isEnglish,
                    onNavigateToIncome = {
                        playButtonSound(context)
                        selectedScreen = Screen.Income
                    },
                    onThemeToggle = onThemeToggle,
                    onLanguageToggle = onLanguageToggle
                )
                Screen.Income -> IncomeScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        selectedScreen = Screen.Home
                    }
                )
                Screen.Input -> InputScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        selectedScreen = Screen.Home
                    },
                    onConfirm = { transaction ->
                        playButtonSound(context)
                        println(transaction)
                    }
                )
                Screen.Outcome -> OutcomeScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        selectedScreen = Screen.Home
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedScreen: Screen, isEnglish: Boolean, onScreenSelected: (Screen) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedScreen == Screen.Income,
            onClick = { onScreenSelected(Screen.Income) },
            label = { Text(if (isEnglish) "Income" else "รายรับ") },
            icon = {}
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Input,
            onClick = { onScreenSelected(Screen.Input) },
            label = { Text(if (isEnglish) "Input" else "ป้อนข้อมูล") },
            icon = {}
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Outcome,
            onClick = { onScreenSelected(Screen.Outcome) },
            label = { Text(if (isEnglish) "Outcome" else "รายจ่าย") },
            icon = {}
        )
    }
}