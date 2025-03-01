package com.example.projectapp.ui.screens

import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.ProjectApplication
import com.example.projectapp.model.Transaction
import com.example.projectapp.ui.components.TransactionItem
import com.example.projectapp.ui.viewmodel.TransactionViewModel
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun HomeScreen(
    isEnglish: Boolean,
    onNavigateToIncome: () -> Unit,
    onThemeToggle: () -> Unit,
    onLanguageToggle: () -> Unit,
    onNavigateToEdit: (Transaction) -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as ProjectApplication

    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.TransactionViewModelFactory(
            application.repository
        )
    )

    val transactions by viewModel.allTransactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    fun fetchFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchFCMToken()
        }
    }

    fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                fetchFCMToken()
            }
        } else {
            fetchFCMToken()
        }
    }

    LaunchedEffect(Unit) {
        fetchFCMToken()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = if (isEnglish) "Hello" else "สวัสดี", fontSize = 35.sp)
                Text(text = "User", fontSize = 45.sp)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onLanguageToggle,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(90.dp).height(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = if (isEnglish) "🇬🇧 ENG" else "🇹🇭 TH", fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onThemeToggle,
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🌙", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { askNotificationPermission() },
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("\uD83D\uDD14", fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(colors = listOf(Color(0xFF7F9167), Color(0xFFD7E8C2)))
                ).padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(text = if (isEnglish) "Income" else "รายรับ", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$${"%,.2f".format(totalIncome ?: 0.0)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(100.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.linearGradient(colors = listOf(Color(0xFF5E1818), Color(0xFFCF7E7E)))
                ).padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(text = if (isEnglish) "Outcome" else "รายจ่าย", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$${"%,.2f".format(totalOutcome ?: 0.0)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            val groupedData = transactions.groupBy { it.date }
            groupedData.forEach { (date, transactions) ->
                item {
                    Text(text = date, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                }
                items(transactions) { transaction ->
                    TransactionItem(transaction, isEnglish, { onNavigateToEdit(transaction) }) {
                        transactionToDelete = transaction
                        showDeleteConfirmation = true
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showDeleteConfirmation && transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(if (isEnglish) "Confirm Delete" else "ยืนยันการลบ") },
            text = { Text(if (isEnglish) "Are you sure you want to delete \"${transactionToDelete?.title}\"?" else "คุณแน่ใจหรือไม่ว่าต้องการลบ \"${transactionToDelete?.title}\"?") },
            confirmButton = {
                Button(onClick = {
                    transactionToDelete?.let { viewModel.deleteTransaction(it) }
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
