package com.example.projectapp

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectapp.ui.theme.ProjectTheme
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            ProjectTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { 
                        playButtonSound(this)
                        isDarkTheme = !isDarkTheme 
                    }
                )
            }
        }
    }
}


fun playButtonSound(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.button)
    mediaPlayer?.start()
    mediaPlayer?.setOnCompletionListener {
        it.release() // Release resources when playback is complete
    }
}

@Composable
fun MainScreen(isDarkTheme: Boolean, onThemeToggle: () -> Unit) {
    var selectedScreen by remember { mutableStateOf(Screen.Home) }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedScreen) { screen ->
                playButtonSound(context)
                selectedScreen = screen
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                Screen.Home -> HomeScreen(
                    onNavigateToIncome = { 
                        playButtonSound(context)
                        selectedScreen = Screen.Income 
                    },
                    onThemeToggle = onThemeToggle
                )
                Screen.Income -> IncomeScreen { 
                    playButtonSound(context)
                    selectedScreen = Screen.Home 
                }
                Screen.Input -> InputScreen(
                    onBack = { 
                        playButtonSound(context)
                        selectedScreen = Screen.Home 
                    },
                    onConfirm = { transaction ->
                        playButtonSound(context)
                        println(transaction)
                    }
                )
                Screen.Outcome -> OutcomeScreen { 
                    playButtonSound(context)
                    selectedScreen = Screen.Home 
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = selectedScreen == Screen.Income,
            onClick = { onScreenSelected(Screen.Income) },
            label = { Text("Income") },
            icon = {}
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Input,
            onClick = { onScreenSelected(Screen.Input) },
            label = { Text("Input") },
            icon = {}
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Outcome,
            onClick = { onScreenSelected(Screen.Outcome) },
            label = { Text("Outcome") },
            icon = {}
        )
    }
}


@Composable
fun HomeScreen(onNavigateToIncome: () -> Unit, onThemeToggle: () -> Unit) {
    val totalIncome = transactions.filter { it.type == "Income" }
        .sumOf { it.amount.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0 }

    val totalOutcome = transactions.filter { it.type == "Outcome" }
        .sumOf { it.amount.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0 }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                        Text(text = "Sawasdeesssss", fontSize = 35.sp)
            Text(text = "Kasidis", fontSize = 45.sp)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(90.dp)
                        .height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🇬🇧 ENG", fontSize = 20.sp, textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onThemeToggle,
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🌙", fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF5E1818), Color(0xFFCF7E7E))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text("Outcome", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$${"%,.2f".format(totalOutcome)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFD7E8C2), Color(0xFF7F9167))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text("Income", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$${"%,.2f".format(totalIncome)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val groupedData = transactions.groupBy { it.date }

        LazyColumn(modifier = Modifier.weight(1f)) {
            groupedData.forEach { (date, transactions) ->
                item {
                    Text(
                        text = date,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(onBack: () -> Unit, onConfirm: (Transaction) -> Unit) {
    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Outcome") }
    var expanded by remember { mutableStateOf(false) }
    val transactionTypes = listOf("Income", "Outcome")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Adding", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Column {
            Text(text = "Name", fontSize = 14.sp, color = Color.Gray)
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
        }

        Column {
            Text(text = "Value", fontSize = 14.sp, color = Color.Gray)
            TextField(
                value = value,
                onValueChange = {
                    val cleaned = it.replace("[^\\d.]".toRegex(), "")
                    value = if (cleaned.isNotEmpty()) "$${"%,.2f".format(cleaned.toDoubleOrNull() ?: 0.0)}" else ""
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Column {
            Text(text = "Type", fontSize = 14.sp, color = Color.Gray)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    transactionTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && value.isNotEmpty()) {
                    val formattedValue = value.replace("[^\\d.]".toRegex(), "")
                    val transaction = Transaction(
                        date = "07 April",
                        title = name,
                        type = selectedType,
                        amount = if (selectedType == "Outcome") "-$$formattedValue" else "$$formattedValue",
                        tax = "Tax $$formattedValue"
                    )

                    println("Transaction created: $transaction")

                    onConfirm(transaction)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            enabled = name.isNotEmpty() && value.isNotEmpty()
        ) {
            Text(text = "Confirm", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun IncomeScreen(onBack: () -> Unit) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(0) }
    val transactions = transaction_Income.sortedBy { it.date }
    val totalPages = (transactions.size + pageSize - 1) / pageSize
    val paginatedTransactions = transactions.drop(currentPage * pageSize).take(pageSize)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) { Text("Back") }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Income", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        LazyColumn(modifier = Modifier.weight(1f)) {
            paginatedTransactions.groupBy { it.date }.forEach { (date, transactions) ->
                item {
                    Text(
                        text = date,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { if (currentPage > 0) currentPage-- }, enabled = currentPage > 0) {
                Text("Previous")
            }
            Text(text = "Page ${currentPage + 1} of $totalPages")
            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun OutcomeScreen(onBack: () -> Unit) {
    val pageSize = 7
    var currentPage by remember { mutableStateOf(0) }
    val transactions = transaction_outcome.sortedBy { it.date }
    val totalPages = (transactions.size + pageSize - 1) / pageSize
    val paginatedTransactions = transactions.drop(currentPage * pageSize).take(pageSize)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("Back") }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Outcome", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        LazyColumn(modifier = Modifier.weight(1f)) {
            paginatedTransactions.groupBy { it.date }.forEach { (date, transactions) ->
                item {
                    Text(
                        text = date,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { if (currentPage > 0) currentPage-- }, enabled = currentPage > 0) {
                Text("Previous")
            }
            Text(text = "Page ${currentPage + 1} of $totalPages")
            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val amountColor = if (transaction.type == "Outcome") Color.Red else Color.Green

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Column {
                Text(text = transaction.title, fontSize = 18.sp, color = amountColor)
                Text(text = transaction.type, fontSize = 14.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = transaction.amount, fontSize = 18.sp, color = amountColor)
                Text(text = transaction.tax, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

enum class Screen {
    Home, Income, Input, Outcome
}