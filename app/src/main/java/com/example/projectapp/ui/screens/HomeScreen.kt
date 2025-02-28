package com.example.projectapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.ProjectApplication
import com.example.projectapp.model.Transaction
import com.example.projectapp.ui.components.TransactionItem
import com.example.projectapp.ui.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    isEnglish: Boolean,
    onNavigateToIncome: () -> Unit,
    onThemeToggle: () -> Unit,
    onLanguageToggle: () -> Unit,
    onNavigateToEdit: (Transaction) -> Unit = {}
) {
    // Get the repository from the Application class
    val context = LocalContext.current
    val application = context.applicationContext as ProjectApplication

    // Initialize ViewModel with factory
    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.TransactionViewModelFactory(
            application.repository
        )
    )

    // Collect transaction data as state
    val transactions by viewModel.allTransactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()

    // For confirmation dialog
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = if (isEnglish) "Hello" else "สวัสดี",
                    fontSize = 35.sp
                )
                Text(text = "Kasidis", fontSize = 45.sp)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onLanguageToggle,
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(90.dp)
                        .height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isEnglish) "🇬🇧 ENG" else "🇹🇭 TH",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
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
                    Text(
                        text = if (isEnglish) "Outcome" else "รายจ่าย",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$${"%,.2f".format(totalOutcome ?: 0.0)}",
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
                            colors = listOf(Color(0xFF7F9167), Color(0xFFD7E8C2))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = if (isEnglish) "Income" else "รายรับ",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "$${"%,.2f".format(totalIncome ?: 0.0)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group transactions by date
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
                    TransactionItem(
                        transaction = transaction,
                        isEnglish = isEnglish,
                        onEdit = { onNavigateToEdit(transaction) },
                        onDelete = {
                            transactionToDelete = transaction
                            showDeleteConfirmation = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirmation && transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(if (isEnglish) "Confirm Delete" else "ยืนยันการลบ") },
            text = {
                Text(
                    if (isEnglish)
                        "Are you sure you want to delete \"${transactionToDelete?.title}\"?"
                    else
                        "คุณแน่ใจหรือไม่ว่าต้องการลบ \"${transactionToDelete?.title}\"?"
                )
            },
            confirmButton = {
                Button(onClick = {
                    transactionToDelete?.let { transaction ->
                        viewModel.deleteTransaction(transaction)
                    }
                    showDeleteConfirmation = false
                }) {
                    Text(if (isEnglish) "Delete" else "ลบ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(if (isEnglish) "Cancel" else "ยกเลิก")
                }
            }
        )
    }
}