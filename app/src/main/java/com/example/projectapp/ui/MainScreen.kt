package com.example.projectapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectapp.model.Transaction
import com.example.projectapp.navigation.DeepLinkData
import com.example.projectapp.playButtonSound
import com.example.projectapp.ui.components.BottomNavBar
import com.example.projectapp.ui.screens.*

@Composable
fun MainScreenWithNavHost(
    isDarkTheme: Boolean,
    isEnglish: Boolean,
    onThemeToggle: () -> Unit,
    onLanguageToggle: () -> Unit,
    deepLinkData: DeepLinkData? = null
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // For passing deep link data to InputScreen
    var initialTitle by remember { mutableStateOf<String?>(null) }
    var initialAmount by remember { mutableStateOf<String?>(null) }
    var initialType by remember { mutableStateOf<String?>(null) }

    // State to hold the transaction being edited
    var transactionToEdit by remember { mutableStateOf<Transaction?>(null) }

    // Handle deep linking
    LaunchedEffect(deepLinkData) {
        deepLinkData?.let {
            // Store parameters for when InputScreen renders
            initialTitle = it.title
            initialAmount = it.amount
            initialType = it.type

            // Navigate to destination
            navController.navigate(it.destination)
        }
    }

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
                    onLanguageToggle = onLanguageToggle,
                    onNavigateToEdit = { transaction ->
                        playButtonSound(context)
                        transactionToEdit = transaction
                        navController.navigate("edit")
                    }
                )
            }

            composable("income") {
                IncomeScreen(
                    isEnglish = isEnglish,
                    onBack = {
                        playButtonSound(context)
                        navController.navigateUp()
                    },
                    onNavigateToEdit = { transaction ->
                        playButtonSound(context)
                        transactionToEdit = transaction
                        navController.navigate("edit")
                    }
                )
            }

            composable("input") {
                InputScreen(
                    isEnglish = isEnglish,
                    initialTitle = initialTitle,
                    initialAmount = initialAmount,
                    initialType = initialType,
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
                    },
                    onNavigateToEdit = { transaction ->
                        playButtonSound(context)
                        transactionToEdit = transaction
                        navController.navigate("edit")
                    }
                )
            }

            composable("edit") {
                // Only show the edit screen if we have a transaction to edit
                transactionToEdit?.let { transaction ->
                    EditTransactionScreen(
                        transaction = transaction,
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
}