package com.example.projectapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectapp.model.Transaction

@Composable
fun TransactionItem(transaction: Transaction, isEnglish: Boolean) {
    val amountColor = if (transaction.type == "Outcome") Color(0xFFA51D32 ) else Color(0xFF6B8E23)

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
}