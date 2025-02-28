package com.example.projectapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectapp.model.Transaction

@Composable
fun TransactionItem(
    transaction: Transaction,
    isEnglish: Boolean,
    onEdit: (Transaction) -> Unit = {},
    onDelete: (Transaction) -> Unit = {}
) {
    val amountColor = if (transaction.type == "Outcome") Color(0xFFA51D32) else Color(0xFF6B8E23)
    var showOptions by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { showOptions = true }
        ) {
            Column {
                Text(
                    text = transaction.title,
                    fontSize = 18.sp,
                    color = amountColor
                )
                Text(
                    text = if (isEnglish)
                        transaction.type
                    else
                        if (transaction.type == "Income") "รายรับ" else "รายจ่าย",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.amount,
                    fontSize = 18.sp,
                    color = amountColor
                )
                Text(
                    text = transaction.tax,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text(if (isEnglish) "Transaction Options" else "ตัวเลือกรายการ") },
            text = { Text(if (isEnglish) "What would you like to do with this transaction?" else "คุณต้องการทำอะไรกับรายการนี้?") },
            confirmButton = {
                Column {
                    Button(
                        onClick = {
                            onEdit(transaction)
                            showOptions = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isEnglish) "Edit" else "แก้ไข")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            onDelete(transaction)
                            showOptions = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isEnglish) "Delete" else "ลบ")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showOptions = false }) {
                    Text(if (isEnglish) "Cancel" else "ยกเลิก")
                }
            }
        )
    }
}