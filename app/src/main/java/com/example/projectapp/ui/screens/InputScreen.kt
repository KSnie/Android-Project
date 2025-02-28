package com.example.projectapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectapp.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(isEnglish: Boolean, onBack: () -> Unit, onConfirm: (Transaction) -> Unit) {
    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(if (isEnglish) "Outcome" else "รายจ่าย") }
    var expanded by remember { mutableStateOf(false) }
    val transactionTypes = if (isEnglish)
        listOf("Income", "Outcome")
    else
        listOf("รายรับ", "รายจ่าย")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onBack) {
            Text(if (isEnglish) "Back" else "กลับ")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = if (isEnglish) "Adding" else "เพิ่มรายการ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Column {
            Text(
                text = if (isEnglish) "Name" else "ชื่อ",
                fontSize = 14.sp,
                color = Color.Gray
            )
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
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Column {
            Text(
                text = if (isEnglish) "Value" else "จำนวนเงิน",
                fontSize = 14.sp,
                color = Color.Gray
            )
            TextField(
                value = value,
                onValueChange = {
                    val cleaned = it.replace("[^\\d.]".toRegex(), "")
                    value = if (cleaned.isNotEmpty())
                        "$${"%,.2f".format(cleaned.toDoubleOrNull() ?: 0.0)}"
                    else ""
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
            Text(
                text = if (isEnglish) "Type" else "ประเภท",
                fontSize = 14.sp,
                color = Color.Gray
            )
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
                        type = if (isEnglish) {
                            if (selectedType == "Income") "Income" else "Outcome"
                        } else {
                            if (selectedType == "รายรับ") "Income" else "Outcome"
                        },
                        amount = if (selectedType == "Outcome" || selectedType == "รายจ่าย")
                            "-$$formattedValue"
                        else
                            "$$formattedValue",
                        tax = if (isEnglish)
                            "Tax $$formattedValue"
                        else
                            "ภาษี $$formattedValue"
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
            Text(
                text = if (isEnglish) "Confirm" else "ยืนยัน",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}