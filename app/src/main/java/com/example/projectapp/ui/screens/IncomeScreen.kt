package com.example.projectapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.ProjectApplication
import com.example.projectapp.model.Transaction
import com.example.projectapp.model.TransactionEntity
import com.example.projectapp.ui.components.TransactionItem
import com.example.projectapp.ui.viewmodel.TransactionViewModel

@Composable
fun IncomeScreen(
    isEnglish: Boolean,
    onBack: () -> Unit,
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

    // Collect income transactions as state
    val transactions by viewModel.incomeTransactions.collectAsState()

    // For confirmation dialog
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    val pageSize = 7
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = (transactions.size + pageSize - 1) / pageSize
    val paginatedTransactions = transactions.drop(currentPage * pageSize).take(pageSize)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text(if (isEnglish) "Back" else "กลับ")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isEnglish) "Income" else "รายรับ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

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

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text(if (isEnglish) "Previous" else "ก่อนหน้า")
            }

            Text(
                text = if (isEnglish)
                    "Page ${currentPage + 1} of $totalPages"
                else
                    "หน้า ${currentPage + 1} จาก $totalPages"
            )

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text(if (isEnglish) "Next" else "ถัดไป")
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